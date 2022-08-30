package top.starshine.mq.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.stereotype.Service;
import top.starshine.commons.entity.coupon.Coupon;
import top.starshine.commons.entity.coupon.RollbackCouponDto;
import top.starshine.commons.entity.order.OrderDetail;
import top.starshine.commons.entity.order.RefundRecord;
import top.starshine.commons.entity.order.TradeOrderDto;
import top.starshine.commons.entity.order.TradeRefundDto;
import top.starshine.commons.model.mq.RabbitTopic;

/**
 * <h3>支付发送消息生产者</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/2  下午 10:28  周二
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentSendMQ extends RabbitTopic {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 发送 已使用的回滚消息
     * @param payload 载荷数据
     */
    public void sendRollbackCouponUse(RollbackCouponDto payload){
        this.rabbitTemplate.convertAndSend(EXCHANGE_COUPON, ROUTING_ROLLBACK_COUPON_USE, payload, message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            // 设置消息的持久化策略
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            // 设置额外的参数,消息头的属性的对象
            messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, payload.getClass());
            return message;
        });
    }

}
