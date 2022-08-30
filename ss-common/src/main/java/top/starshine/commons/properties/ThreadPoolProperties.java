package top.starshine.commons.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "starshine.thread.pool")
public class ThreadPoolProperties {

    /** 核心池大小 */
    private int corePoolSize;

    /** 最大池大小 */
    private int maximumPoolSize;

    /** 保持活动时间 */
    private int keepAliveTime;

    /** 工作队列大小 */
    private int workQueueSize;

}
