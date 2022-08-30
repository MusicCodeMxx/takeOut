package top.starshine.mq.bean;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.starshine.commons.model.mq.RabbitTopic;

/**
 * <h3>信息队列 ，通知卖家处理退款</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/4  下午 10:39  周四
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Configuration
public class NoticeSellerHandleRefundTopicDeadConfig extends RabbitTopic {


    /**
     * 通知卖家处理退款的消费队列
     * @return 队列对象
     */
    @Bean(name = "createNoticeSellerHandleRefundTopicConsumerQueue")
    public Queue createTopicConsumerQueue(){
        // 创建队列并返回死信队列实例
        return QueueBuilder.durable(QUEUE_NOTICE_SELLER_HANDLE_REFUND).build();
    }

    /**
     * <h3>这里注意, 使用的交换机是订单交换机, 必须有者 bean 存在</h3>
     * 把正常的队列和正常的路由 key 绑定到死信队列
     * @param queue 队列
     * @param exchange 交换机对象
     * @return 绑定对象
     */
    @Bean(name = "noticeSellerHandleRefundBindingTopicOrderExConsumer")
    public Binding bindingTopicExConsumer(@Qualifier("createNoticeSellerHandleRefundTopicConsumerQueue") Queue queue,
                                          @Qualifier("createCancelAnOrderTopicExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_NOTICE_SELLER_HANDLE_REFUND).noargs();
    }


}
