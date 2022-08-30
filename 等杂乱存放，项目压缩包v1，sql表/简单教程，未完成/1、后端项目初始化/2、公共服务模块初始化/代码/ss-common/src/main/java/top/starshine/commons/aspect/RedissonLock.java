package top.starshine.commons.aspect;

import java.lang.annotation.*;

/**
 * <h3> redis 锁</h3>
 * <p>
 *     1、无返回值
 *     2、如果没有拿到锁就阻塞等待锁释放, 拿不到锁会一直等待
 * </p>
 * <ul>
 *     <li>value 锁名称</li>
 *     <li>leaseTime 租期, 拿到锁的自动过期时间, 单位(SECONDS)/秒</li>
 * </ul>
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/30  下午 8:09  周六
 * @Description: hello world
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedissonLock {

    /**锁名称*/
    String value() default "lock:aop:user:id:";

    /**<h3>租期, 拿到锁的自动过期时间, 单位(SECONDS)/秒, 默认 30 秒和看门狗检查</h3>*/
    long leaseTime() default 0L;

}
