package top.starshine.commons.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
@ConfigurationProperties(prefix = "starshine.async.thread.pool")
public class AsyncThreadPoolTaskProperties {

    /** 核心线程数 */
    private int corePoolSize = 10;

    /** 最大线程数 */
    private int maximumPoolSize = 50;

    /** 队列最大长度 */
    private Integer queueCapacity = Integer.MAX_VALUE;

    /**允许超时关闭*/
    private Boolean allowCoreThreadTimeOut = false;

    /** 保持活动时间,保持存活时间 */
    private int keepAliveTime = 60;

    /** 工作队列大小 */
    private int workQueueSize = 2000;

    /**异步线程池名前缀*/
    private String threadNamePrefix = "ss-async-";

}
