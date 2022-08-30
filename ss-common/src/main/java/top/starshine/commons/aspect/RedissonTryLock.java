package top.starshine.commons.aspect;

import java.lang.annotation.*;

/**
 * <h3> redis 尝试锁</h3>
 * <p>
 *     1、返回值布尔
 *     2、获取到锁返回 true, 获取不到锁并直接返回 false
 *     3、指定时间内去尝试拿锁, 拿不到就返回false, 拿到返回true。
 * </p>
 * <ul>
 *     <li>value 锁名称</li>
 *     <li>leaseTime 租期, 拿到锁的自动过期时间, 单位(SECONDS)/秒</li>
 *     <li>waitTime 等待锁的时间, 定时间内去尝试拿锁, 单位(SECONDS)/秒</li>
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
public @interface RedissonTryLock {

    /**锁名称*/
    String value() default "lock:try:aop:user:id:";

    /**租期, 拿到锁的自动过期时间*/
    long leaseTime() default 30L;

    /**等待锁的时间*/
    long waitTime() default 10L;


}
