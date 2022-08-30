package top.starshine.mq.bean;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.starshine.commons.model.mq.RabbitTopic;

/**
 * <h3>消息队列绑定配置</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/26  下午 9:51  周二
 * @Description: hello world
 */
@Configuration
public class ShortMessageTopicDeadConfig extends RabbitTopic {

    // 短信交换机
    @Bean(name = "shortMessageCreateTopicExchange")
    public Exchange shortMessageExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_SHORT_MESSAGE).durable(true).build();
    }

    // 验证码队列
    @Bean(name = "shortMessageCreateTopicConsumerQueue")
    public Queue shortMessageQueue(){
        return QueueBuilder.durable(QUEUE_SHORT_MESSAGE).build();
    }

    // 验证码绑定
    @Bean
    public Binding shortMessageBinding(@Qualifier("shortMessageCreateTopicExchange") Exchange exchange,
                                        @Qualifier("shortMessageCreateTopicConsumerQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_SHORT_MESSAGE).noargs();
    }

}
