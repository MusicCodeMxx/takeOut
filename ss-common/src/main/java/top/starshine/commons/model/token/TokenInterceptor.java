package top.starshine.commons.model.token;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.exception.InternalServerErrorException;
import top.starshine.commons.exception.UnauthorizedException;
import top.starshine.commons.handle.ThreadLocalCache;
import top.starshine.commons.model.redis.CachePrefix;
import top.starshine.commons.properties.SystemSettingProperties;
import top.starshine.commons.status.BasisStatus;
import top.starshine.commons.status.TokenStatus;
import top.starshine.commons.util.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 用户令牌拦截器
 * @author: starshine
 * @version: 1.0
 * @since: 2022/4/11  下午 6:24  周一
 * @Description:
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {

    private final RedisTemplate redisTemplate;
    private final LoadUserDetailService loadUserDetailService;
    private final IgnoringUrlProperties ignoringUrlProperties;
    private final SystemSettingProperties systemSettingProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 请求头中获取令牌
        String token = TokenUtils.parseHeader(request);

        // 检查是否有令牌,没有就放行
        if (null == token) return true;

        // 放行指定的路径
        if (ignoringUrlProperties.checkUrl(request.getRequestURI())) return true;

        /* 解析令牌,检查令牌是否过期了,过期就直接抛异常 */
        DecodedJWT decodedJWT = TokenUtils.parseTokenRemovePrefix(token);
        if ( null == decodedJWT) throw new UnauthorizedException(TokenStatus.INVALID_TOKEN);
        token = null;

        // 检查是否获取到主键
        String id = TokenUtils.getSubject(decodedJWT);
        if (null == id) throw new UnauthorizedException(TokenStatus.TOKEN_PAYLOAD_IS_EMPTY);

        /* 检查令牌距离过期还有多少时间,检查符合边界操作 */
        long expiresAtToMinutes = TokenUtils.getExpiresAtToMinutes(decodedJWT);
        if ( TokenUtils.TOKEN_RENEWAL_MIN > expiresAtToMinutes) throw new UnauthorizedException(TokenStatus.TOKEN_EXPIRED);
        decodedJWT = null;

        /* 检查是否黑名单,是黑名单就直接抛异常 */
        Object obj = redisTemplate.opsForHash().get(CachePrefix.LOGOUT_STATUS_USER_LIST, id);
        if (null != obj) throw new UnauthorizedException(TokenStatus.YOU_ARE_LOGGED_OUT);

        User user = null;
        try {
            ValueOperations operations = redisTemplate.opsForValue();
            /* 获取缓存中的用户信息 */
            user = (User) operations.get(CachePrefix.LOGIN_STATUS_USER_DETAIL + id);
            // 缓存中的用户信息过期
            if (null == user) {
                user = loadUserDetailService.getUserById(id);
                // 获取为空, 抛空用户异常
                if (null == user) throw new UnauthorizedException(BasisStatus.UNAUTHORIZED);
                // 重入缓存
                operations.set(CachePrefix.LOGIN_STATUS_USER_DETAIL + user.getId(), user,
                        systemSettingProperties.getUserCacheTime(), TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException(TokenStatus.ERROR);
        }

        // 续期检查是否符合操作
        if (expiresAtToMinutes < TokenUtils.TOKEN_RENEWAL_MAX) TokenUtils.addHeaderToken(response, TokenUtils.create(user.getNickname(), id));

        ThreadLocalCache.put(user);// 存入本地线程缓存

        return true;// 放行
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        ThreadLocalCache.remove();// 移除缓存
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ThreadLocalCache.remove();// 移除缓存
    }

}
