package top.starshine.mq.bean;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.starshine.commons.model.mq.RabbitTopic;

/**
 * <h3>订单生成保存 消息队列</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/3  下午 1:35  周三
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Configuration
public class OrderGenerateSaveTopicDeadConfig extends RabbitTopic {

    /**
     * 创建30分钟过期以后消费的队列
     * 也就是死信队列过期的消息，最终会转移到这里
     * @return 队列对象
     */
    @Bean(name = "createOrderGenerateSaveTopicConsumerQueue")
    public Queue createTopicConsumerQueue(){
        // 创建队列并返回死信队列实例
        return QueueBuilder.durable(QUEUE_ORDER_GENERATE_SAVE).build();
    }

    /**
     * <h3>这里注意, 使用的交换机是订单交换机, 必须有者 bean 存在</h3>
     * 把正常的队列和正常的路由key+绑定到死信队列
     * @param queue 队列
     * @param exchange 交换机对象
     * @return 绑定对象
     */
    @Bean(name = "orderGenerateSaveBindingTopicOrderExConsumer")
    public Binding bindingTopicExConsumer(@Qualifier("createOrderGenerateSaveTopicConsumerQueue") Queue queue,
                                          @Qualifier("createCancelAnOrderTopicExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_ORDER_GENERATE_SAVE).noargs();
    }

}
