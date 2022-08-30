package top.starshine.commons.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.starshine.commons.aspect.PreventDuplicateSubmissions;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.exception.BadRequestException;
import top.starshine.commons.handle.ThreadLocalCache;
import top.starshine.commons.status.BasisStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <h3>限流操作</h3>
 * @author: starshine
 * @version: 1.0
 * @since: 2022/7/13  下午 3:42  周三
 * @Description:
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class PreventDuplicateSubmissionsInterceptor {

    private final RedisTemplate redisTemplate;

    @Before("@annotation(preventDuplicateSubmissions)")
    public void interceptor(PreventDuplicateSubmissions preventDuplicateSubmissions) {
        log.info("进入限流管控");
        long increment;
        try {
            // 获取请求的 url
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            StringBuilder stringBuilder = new StringBuilder("Limiting:");
            stringBuilder.append(request.getRequestURI()).append(":").append(request.getRemoteHost());
            User user = ThreadLocalCache.get();
            // 检查是否有登录用户信息
            if (null != user) stringBuilder.append(":").append(user.getId()).append(":").append(user.getPhoneNumber());
            RedisAtomicLong entityIdCounter = new RedisAtomicLong(stringBuilder.toString(), Objects.requireNonNull(redisTemplate.getConnectionFactory()));
            increment = entityIdCounter.getAndIncrement();
            int expire = preventDuplicateSubmissions.expire();
            //初始设置过期时间
            if (increment == 0 && expire > 0) entityIdCounter.expire(expire, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return ;
        }
        // 如果超过设置的参数，则表示重复提交了
        if ((int) increment >= preventDuplicateSubmissions.value()) throw new BadRequestException(BasisStatus.LIMIT_ERROR);
    }

}
