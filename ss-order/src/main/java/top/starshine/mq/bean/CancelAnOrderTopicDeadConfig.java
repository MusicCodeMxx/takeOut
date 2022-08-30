package top.starshine.mq.bean;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.starshine.commons.model.mq.RabbitTopic;

import java.util.HashMap;
import java.util.Map;

/**
 * <h3>检查订单是否超时未支付自动取消 消息队列</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/2  下午 10:05  周二
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Configuration
public class CancelAnOrderTopicDeadConfig extends RabbitTopic {

    /**
     * <h3>订单微服务交换机</h3>
     * 创建生产者的交换机
     * @return 交换机对象
     */
    @Bean(name = "createCancelAnOrderTopicExchange")
    public Exchange createTopicOrderExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_ORDER).durable(true).build();
    }

    /**
     * 创建死信队列
     * @return 队列对象
     */
    @Bean(name = "createCancelAnOrderTopicDeadQueue")
    public Queue createTopicDeadQueue(){
        // 使用Map存放死信队列的三个核心组成部分
        Map<String, Object> args = new HashMap<>();
        // 创建死信队列交换机
        args.put("x-dead-letter-exchange", EXCHANGE_ORDER);
        // 创建死信队列路由
        args.put("x-dead-letter-routing-key", ROUTING_DEAD_CANCEL_AN_ORDER);
        // 设定 TTL，单位是ms，下面的单位是 10s
        args.put("x-message-ttl", CANCEL_AN_ORDER_DEAD_TTL);
        // 创建队列并返回死信队列实例
        return QueueBuilder.durable(QUEUE_DEAD_CANCEL_AN_ORDER).withArguments(args).build();
    }

    /**
     * 创建30分钟过期以后消费的队列
     * 也就是死信队列过期的消息，最终会转移到这里
     * @return 队列对象
     */
    @Bean(name = "createCancelAnOrderTopicConsumerQueue")
    public Queue createTopicConsumerQueue(){
        // 创建队列并返回死信队列实例
        return QueueBuilder.durable(QUEUE_CONSUMER_CANCEL_AN_ORDER).build();
    }

    /**
     * 把正常的队列和正常的路由key+绑定到死信队列
     * @param queue 队列
     * @param exchange 交换机对象
     * @return 绑定对象
     */
    @Bean(name = "cancelAnOrderBindingTopicExDead")
    public Binding bindingTopicExDead(@Qualifier("createCancelAnOrderTopicDeadQueue") Queue queue,
                                           @Qualifier("createCancelAnOrderTopicExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_CANCEL_AN_ORDER_ORDER).noargs();
    }

    /**
     * 把正常的队列和正常的路由key+绑定到死信队列
     * @param queue 队列
     * @param exchange 交换机对象
     * @return 绑定对象
     */
    @Bean(name = "cancelAnOrderBindingTopicOrderExConsumer")
    public Binding bindingTopicExConsumer(@Qualifier("createCancelAnOrderTopicConsumerQueue") Queue queue,
                                               @Qualifier("createCancelAnOrderTopicExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_DEAD_CANCEL_AN_ORDER).noargs();
    }

}
