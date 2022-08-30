package top.starshine.mq.bean;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.starshine.commons.model.mq.RabbitTopic;

/**
 * <h3>回滚已使用的优惠券</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/5  下午 4:29  周五
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Configuration
public class RollbackCouponUseTopicDeadConfig extends RabbitTopic {

    /**
     * <h3>优惠券业务的交换机</h3>
     * 创建生产者的交换机
     * @return 交换机对象
     */
    @Bean(name = "createCouponTopicExchange")
    public Exchange createTopicOrderExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_COUPON).durable(true).build();
    }

    /**
     * 通知 回滚优惠券使用
     * @return 队列对象
     */
    @Bean(name = "createRollbackCouponUseTopicConsumerQueue")
    public Queue createTopicConsumerQueue(){
        // 创建队列并返回死信队列实例
        return QueueBuilder.durable(QUEUE_ROLLBACK_COUPON_USE).build();
    }

    /**
     * <h3>这里注意, 使用的交换机是订单交换机, 必须有者 bean 存在</h3>
     * 把正常的队列和正常的路由 key 绑定到死信队列
     * @param queue 队列
     * @param exchange 交换机对象
     * @return 绑定对象
     */
    @Bean(name = "rollbackCouponUseBindingTopicOrderExConsumer")
    public Binding bindingTopicExConsumer(@Qualifier("createRollbackCouponUseTopicConsumerQueue") Queue queue,
                                          @Qualifier("createCouponTopicExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_ROLLBACK_COUPON_USE).noargs();
    }

}
