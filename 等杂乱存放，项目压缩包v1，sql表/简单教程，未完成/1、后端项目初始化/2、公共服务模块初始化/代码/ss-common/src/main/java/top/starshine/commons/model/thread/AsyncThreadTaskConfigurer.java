package top.starshine.commons.model.thread;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import top.starshine.commons.properties.AsyncThreadPoolTaskProperties;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <h3>异步线程池配置</h3>
 * <p>必须要注入对象方式才能实现异步</p>
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/27  下午 5:00  周三
 * @Description: hello world
 */
//@EnableAsync
@Configuration
@RequiredArgsConstructor
public class AsyncThreadTaskConfigurer implements AsyncConfigurer {

    private final AsyncThreadPoolTaskProperties threadPoolProperties;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 核心线程数，默认为5
        executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
        // 最大线程数，默认为10
        executor.setMaxPoolSize(threadPoolProperties.getMaximumPoolSize());
        // 队列最大长度，一般需要设置值为足够大
        executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        // 线程池维护线程所允许的空闲时间，默认为60s
        executor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveTime());
        // 设置线程池的前缀,注意有长度限制
        executor.setThreadNamePrefix(threadPoolProperties.getThreadNamePrefix());
        // 允许超时关闭
        executor.setAllowCoreThreadTimeOut(threadPoolProperties.getAllowCoreThreadTimeOut());

        // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        executor.setAwaitTerminationSeconds(60);
        // rejection-policy：当 pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 初始化
        executor.initialize();
        return executor;
    }

}
