package top.starshine.commons.aspect;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import top.starshine.commons.model.thread.MyThreadPoolImportSelector;

import java.lang.annotation.*;

/**
 * <h3>是否开启自定义线程池</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/29  上午 12:45  周五
 * @Description: hello world
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({MyThreadPoolImportSelector.class})
public @interface EnableMyThreadPool {

    AdviceMode mode() default AdviceMode.PROXY;

}
