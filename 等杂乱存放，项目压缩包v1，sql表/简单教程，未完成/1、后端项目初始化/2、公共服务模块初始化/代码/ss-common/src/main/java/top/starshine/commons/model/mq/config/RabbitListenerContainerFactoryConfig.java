package top.starshine.commons.model.mq.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 兔子监听器容器工厂配置类
 * @author: starshine
 * @version: 1.0
 * @since: 2022/7/7  下午 9:38  周四
 * @Description:
 */
@Configuration
public class RabbitListenerContainerFactoryConfig {

    /**
     * 自动确认应答配置模式：Auto - ACK
     * @param connectionFactory 缓存连接工厂实例
     * @return 简单的兔子监听器容器工厂
     */
    @Bean("simpleTopicOrderDeadContainerAutoACK")
    public SimpleRabbitListenerContainerFactory simpleTopicOrderDeadContainerAutoACK(CachingConnectionFactory connectionFactory){
        // 返回监听器容器工厂实例
        return generateFactory(1,1,1,connectionFactory, AcknowledgeMode.AUTO);
    }

    /**
     * 手动确认应答配置模式是：Manual - ACK
     * @param connectionFactory 缓存连接工厂实例
     * @return 简单的兔子监听器容器工厂
     */
    @Bean("simpleTopicOrderDeadContainerManualACK")
    public SimpleRabbitListenerContainerFactory simpleTopicOrderDeadContainerManualACK(CachingConnectionFactory connectionFactory){
        // 返回监听器容器工厂实例
        return generateFactory(1,1,1,connectionFactory, AcknowledgeMode.MANUAL);
    }

    /**
     * 公共配置简单的兔子监听器容器工厂
     * @param concurrentConsumers  设置消息并发实例
     * @param maxConcurrentConsumers 设置消费者并发最大数量实例
     * @param prefetchCount 设置消费每个并发实例预拉取的消息数据量
     * @param connectionFactory 缓存连接工厂实例
     * @param acknowledgeMode 配置确认模式枚举
     * @return 简单的兔子监听器容器工厂
     */
    private SimpleRabbitListenerContainerFactory generateFactory(int concurrentConsumers,
                                                                 int maxConcurrentConsumers,
                                                                 int prefetchCount,
                                                                 CachingConnectionFactory connectionFactory,
                                                                 AcknowledgeMode acknowledgeMode){
        // 创建消息监听器所在的容器工厂实例
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        // 为容器工厂实例设置链接工厂
        factory.setConnectionFactory(connectionFactory);
        // 设置消息在传输过程中的格式为JSON
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        // 设置消息并发实例,这里采用单一模式
        factory.setConcurrentConsumers(concurrentConsumers);
        // 设置消费者并发最大数量实例
        factory.setMaxConcurrentConsumers(maxConcurrentConsumers);
        // 设置消费每个并发实例预拉取的消息数据量
        factory.setPrefetchCount(prefetchCount);
        // ACK自动模型
        factory.setAcknowledgeMode(acknowledgeMode);
        return factory;
    }

}
