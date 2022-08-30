package top.starshine.service.impl;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.starshine.commons.dubbo.UserDubboService;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.exception.BadRequestException;
import top.starshine.commons.handle.ThreadLocalCache;
import top.starshine.commons.model.redis.CachePrefix;
import top.starshine.commons.properties.SystemSettingProperties;
import top.starshine.commons.status.UserStatus;
import top.starshine.mq.producer.ShortMessageSendMQ;
import top.starshine.service.LoginService;

import java.util.concurrent.TimeUnit;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/26  下午 4:37  周二
 * @Description: hello world
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    @DubboReference(cluster = "failback", interfaceClass = UserDubboService.class, interfaceName = "top.top.starshine.dubbo.UserDubboService", version = "1.0.0")
    private UserDubboService userDubboService;
    private final RedisTemplate redisTemplate;
    private final ShortMessageSendMQ shortMessageSendMQ;
    private final SystemSettingProperties systemSettingProperties;
    private final RedissonClient redissonClient;

    private static final String LOGIN_USER_REGISTER_LOCK_KEY = "lock:login:user:register:";

    @Override
    public void sendMsm(String phoneNumber) {
        // 检查缓存中是否还有验证码
        if (null != redisTemplate.opsForValue().get(CachePrefix.LOGIN_VERIFICATION_CODE + phoneNumber))
            throw new BadRequestException(UserStatus.HAS_NOT_EXPIRED);

        shortMessageSendMQ.sendMsm(phoneNumber);// MQ 生成验证码发送消息
    }

    @Override
    public User submit(String phoneNumber, Integer verificationCode){
        ValueOperations operations = redisTemplate.opsForValue();
        // 验证码是否正确
        Integer codeCache = (Integer) operations.get(CachePrefix.LOGIN_VERIFICATION_CODE + phoneNumber);
        if (null == codeCache) throw new BadRequestException(UserStatus.VERIFICATION_CODE_EXPIRED);
        if (!codeCache.equals(verificationCode)) throw new BadRequestException(400, "验证码输入错误");

        User user = userDubboService.findUserByPhoneNumber(phoneNumber);// 检查用户是否存在
        if (null == user){
            // 生成信息
            user = generateUserDetail(phoneNumber);
            // 为什么这里要存储用户信息？远程调用时候会将用户信息写到请求中, 到生成者过滤器就存入线程缓存
            ThreadLocalCache.put(user);
            RLock lock = redissonClient.getLock(LOGIN_USER_REGISTER_LOCK_KEY + phoneNumber);// 获取锁, 反正重复注册
            try {
                lock.lock();// 加锁
                user = userDubboService.findUserByPhoneNumber(phoneNumber);// 再次检查用户是否存在
                if (null == user) {
                    // 保存到数据库, 可以用 MQ 或者异步, 推荐用 MQ, 我就懒得弄了, 各位就自行选择
                    userDubboService.saveUserDetail(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
                lock.unlock();// 解锁 将设解锁代码没有运行，redissonClient 会不会出现死锁
            }
        }else{
            // 为什么这里要存储用户信息？远程调用时候会将用户信息写到请求中, 到生成者过滤器就存入线程缓存
            ThreadLocalCache.put(user);
            // 解除黑名单
            redisTemplate.opsForHash().delete(CachePrefix.LOGOUT_STATUS_USER_LIST,user.getId().toString());
        }
        if (null ==  user) throw new RuntimeException("");
        // 存入缓存
        operations.set(CachePrefix.LOGIN_STATUS_USER_DETAIL + user.getId(),
                user,systemSettingProperties.getUserCacheTime(), TimeUnit.MINUTES);
        return user;// 返回用户信息
    }

    @Async
    @Override
    public void logout(User user) {
        String userId = String.valueOf(user.getId());
        // 清除用户缓存
        redisTemplate.delete(CachePrefix.LOGIN_STATUS_USER_DETAIL + userId);
        // 拉入黑名单,记录退出登录时间
        redisTemplate.opsForHash().put(CachePrefix.LOGOUT_STATUS_USER_LIST, userId, System.currentTimeMillis());
    }

    // 用户注册信息填充处理
    private User generateUserDetail(String phoneNumber){
        User user = new User();
        user.setSex(0)
            .setStatus(1)
            .setPhoneNumber(phoneNumber)
            .setDescription("快来写下您的个性签名~")
            // 手机尾数作为手机号
            .setNickname(phoneNumber.substring(phoneNumber.length() - 4) + "用户")
            // 随机头像
            .setAvatar("http://10.10.10.55:9000/reggie/static/user/avatar/" + (int)Math.ceil(Math.random() * 10)+ ".jpeg");
        user.setId(new DefaultIdentifierGenerator().nextId(new Object()));
        return user;
    }

}
