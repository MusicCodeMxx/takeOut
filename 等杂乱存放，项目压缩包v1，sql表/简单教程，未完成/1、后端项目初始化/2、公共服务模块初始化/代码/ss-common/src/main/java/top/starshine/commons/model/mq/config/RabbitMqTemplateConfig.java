package top.starshine.commons.model.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <h3>兔子消息队列模板配置类</h3>
 * @author: starshine
 * @version: 1.0
 * @since: 2022/6/23  下午 6:39  周四
 * @Description:
 */
@Slf4j
@Configuration
public class RabbitMqTemplateConfig {

    /**
     * <h3>兔子消息队列实例配置</h3>
     * @param connectionFactory {@link CachingConnectionFactory} 缓存连接工厂实例
     * @return {@link RabbitTemplate} 兔子消息队列模板对象
     */
    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        // 设置消息发送确认机制,生产确认
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        // 设置消息发送确认机制,发送成功返回反馈信息
        connectionFactory.setPublisherReturns(true);
        // 定义RabbitMQ消息操作组件实例
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        /*
            设置mandatory标志说明开启了消息故障检测模式，它只会让RabbitMq向你通知失败，而不会通知成功。
            当交换器根据自身和路由键无法找到合适的队列时，当mandatory设置为true，
            会通过Basic.Return RPC命令将消息返回给发布者；当mandatory设置为false时，该消息被直接丢弃。
            参考地址: https://blog.csdn.net/lvlht/article/details/105604862
            参考地址: https://www.jianshu.com/p/033d7c0d2ca9
         */
        rabbitTemplate.setMandatory(true);
        // 设置发送的格式
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        // 配置回调
        callbackConfigure(rabbitTemplate);
        return rabbitTemplate;
    }

    /**
     * <h3>消息配置回调</h3>
     * <h3>兔子消息队列确认机制处理</h3>
     * <ul>
     *     <li>注意, 当投递到交换机失败, ConfirmCallback 和 ReturnsCallback 回调都执行</li>
     *     <li>参考链接</li>
     *     <li>https://blog.csdn.net/qq_40438883/article/details/120870341</li>
     * </ul>
     * @param rabbitTemplate {@link RabbitTemplate} 兔子消息队列模板对象
     */
    private void callbackConfigure(RabbitTemplate rabbitTemplate){

        // 消息入交换机消息 - 消息进入到交换机触发回调, 无论失败还是成功该方法都调用
        rabbitTemplate.setConfirmCallback(
                /*
                 * correlationData: 消息唯一关联数据
                 * ack:             消息是否成功收到: true 投递成功, false 投递失败
                 * cause:           错误发送的原因
                 */
                (correlationData, ack, cause) -> {
                    if (ack) {
                        log.info("消息投递成功");
                    }else{
                        log.error(">>>>>>>>>>>>>>>>>>>Broker>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                        log.error("消息发送失败,未能送达交换机");
                        log.error("唯一关联数据：{}", correlationData);// 消息主键
                        log.error("错误发送的原因：{}", cause);
                        log.error("<<<<<<<<<<<<<<<<<<<Broker<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                    }

                    // TODO 执行消息保存 ?

                }
        );

        // 发送消息成功后是不会触发这里的
        // 消息入队失败 - 设置消息发送确认机制, 若发送完成之后返回反馈信息, 路由不到队列时触发，成功则不触发
        // returnedMessage = 返回消息
        rabbitTemplate.setReturnsCallback(returnedMessage -> {

            // 错误原因打印
            log.error(">>>>>>>>>>>>>>>>>>>>>Queue>>>>>>>>>>>>>>>>>>>>>");
            log.error("消息发送失败,未能送达队列");
            log.error("消息详情：{}", returnedMessage.getMessage());
            log.error("状态码：{}", returnedMessage.getReplyCode());
            log.error("错误消息：{}", returnedMessage.getReplyText());
            log.error("交换机名称：{}", returnedMessage.getExchange());
            log.error("路由键名称：{}", returnedMessage.getRoutingKey());
            log.error("<<<<<<<<<<<<<<<<<<<<<Queue<<<<<<<<<<<<<<<<<<<<<");

            // TODO 执行消息保存, 只要这里一旦触发，那么一定是持久化失败

        });
    }

}
