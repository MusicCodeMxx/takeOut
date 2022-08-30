package top.starshine.commons.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <h3>线程池配置</h3>
 * @author: starshine
 * @version: 1.0
 * @since: 2022/7/24  下午 3:55  周日
 * @Description:
 */
@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "starshine.thread.pool")
public class ThreadPoolConfig {

    /** 核心池大小 */
    private int corePoolSize;

    /** 最大池大小 */
    private int maximumPoolSize;

    /** 保持活动时间 */
    private int keepAliveTime;

    /** 工作队列大小 */
    private int workQueueSize;

    /**
     * 线程实例对象
     * 链接阻塞双端队列版本
     * @return 线程池执行器实例对象
     */
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(getCorePoolSize(),
                                    getMaximumPoolSize(),
                                    getKeepAliveTime(),
                                    TimeUnit.SECONDS,
                                    new LinkedBlockingDeque<>(getWorkQueueSize()));
    }

}
