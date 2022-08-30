package top.starshine.commons.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h3>对称加密进行解密操作</h3>
 * <ul>
 *     <li>注意的解密数据只能从 body 取出</li>
 *     <li>支持 String 和对象, 不支持基本包装类型</li>
 *     <li>填入要解密的字段名</li>
 *     <li>不填入就全字段解密</li>
 *     <li>解密只能是 String 字段</li>
 * </ul>
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/30  下午 10:31  周六
 * @Description: hello world
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AesDecrypt {

    /**指定解密的字段名称*/
    String[] value() default "";

}
