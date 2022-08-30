package top.starshine.commons.aspect;

import java.lang.annotation.*;

/**
 * <h3>针对结果要直接写入到响应体的需求, 可以添加该注解即可</h3>
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/24  下午 9:56  周日
 * @Description: hello world
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DoWriteBody {

    /**是否直接写入响应体*/
    boolean value() default true;

}
