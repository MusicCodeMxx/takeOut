package top.starshine.commons.model.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * <h3>自定义线程池开关实现类</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/29  上午 12:46  周五
 * @Description: hello world
 */
@Slf4j
public class MyThreadPoolImportSelector implements ImportSelector {

    /**
     * 如果有 @EnableMyThreadPool 则开启自定义线程池
     * @param importingClassMetadata 导入类元数据
     * @return 字符串数组
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{ThreadPoolConfig.class.getName()};
    }

}

