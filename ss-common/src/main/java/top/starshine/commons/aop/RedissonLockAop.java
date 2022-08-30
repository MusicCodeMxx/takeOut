package top.starshine.commons.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import top.starshine.commons.aspect.RedissonLock;
import top.starshine.commons.aspect.RedissonTryLock;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.exception.BussinessException;
import top.starshine.commons.handle.ThreadLocalCache;

import java.util.concurrent.TimeUnit;

/**
 * <h3>Redisson 锁切面</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/30  下午 8:18  周六
 * @Description: hello world
 */
@Slf4j
@Aspect
@Order(1) // 数字小优先
@Component
@RequiredArgsConstructor
public class RedissonLockAop {

    private final RedissonClient redissonClient;

    // 阻塞锁
    @Around("@annotation(top.starshine.commons.aspect.RedissonLock)")
    public Object doLockAfterReturning(ProceedingJoinPoint proceedingJoinPoint) {
        // 获取注解
        RedissonLock annotation = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getAnnotation(RedissonLock.class);
        // 1、获取一把锁,只要锁名字相同就是同一把锁
        RLock lock = redissonClient.getLock(annotation.value() + ((User) ThreadLocalCache.getNotNull()).getId());
        try {
            long leaseTime = annotation.leaseTime();
            // 2、加锁 也可以指定时间, 默认加的锁都是30秒, 若抢不到锁就阻塞等待
            if (1L >= leaseTime ) {
                lock.lock();
            }else {
                lock.lock(annotation.leaseTime(), TimeUnit.SECONDS);
            }
            return proceedingJoinPoint.proceed();// 3、执行具体的方法
        } catch (BussinessException e) {
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }finally {
            // 4、解锁解锁解锁, 一定要解锁
            lock.unlock();
        }
    }

    // 尝试锁
    @Around("@annotation(top.starshine.commons.aspect.RedissonTryLock)")
    public Object doTryLockAfterReturning(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 获取注解
        RedissonTryLock annotation = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getAnnotation(RedissonTryLock.class);
        // 1、获取一把锁,只要锁名字相同就是同一把锁
        RLock lock = redissonClient.getLock(annotation.value() + ((User) ThreadLocalCache.getNotNull()).getId());
        // 2、尝试加锁 也可以指定时间, 默认加的锁都是30秒, 若抢不到锁就阻塞等待
        if (lock.tryLock(annotation.waitTime(), annotation.leaseTime(), TimeUnit.SECONDS)) {
            try {
                return proceedingJoinPoint.proceed();// 3、执行具体的方法
            }finally {
                lock.unlock();// 4、解锁解锁解锁, 一定要解锁
            }
        }else {
            throw new RuntimeException("获取锁失败");
        }
    }

}
