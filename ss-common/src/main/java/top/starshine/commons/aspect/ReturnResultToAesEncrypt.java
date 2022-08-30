package top.starshine.commons.aspect;

import java.lang.annotation.*;

/**
 * <h3>AOP 切面, 将返回结果加密处理</h3>
 * <ul>
 *     <li>支持 String 加密</li>
 *     <li>不支持基本类型, 包含集合</li>
 *     <li>支持对象指定字段, 只支持 String 类型字段</li>
 *     <li>如果不填入字段, 默认 String 类型字段全部加密操作</li>
 * </ul>
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/31  下午 4:50  周日
 * @Description: hello world
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReturnResultToAesEncrypt {

    /**指定加密的字段名称*/
    String[] value() default "";

}
