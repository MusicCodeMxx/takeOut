package top.starshine.commons.aspect;

import java.lang.annotation.*;

/**
 * <h2>防止重复提交注解 - 限流</h2>
 * @author: starshine
 * @version: 1.0
 * @since: 2022/7/11  下午 11:13  周一
 * @Description:
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreventDuplicateSubmissions {

    /**
     * 允许指定的时间内容请求多少次不被拒绝
     * @return int
     */
    int value() default 2;

    /**
     * 过期时间,指定时间内拒绝操作
     * <h3>单位秒(s)</h3>
     * @return int
     */
    int expire() default 3;

}
