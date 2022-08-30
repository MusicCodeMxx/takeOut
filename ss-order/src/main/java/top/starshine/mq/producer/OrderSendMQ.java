package top.starshine.mq.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.stereotype.Service;
import top.starshine.commons.entity.order.OrderDetail;
import top.starshine.commons.entity.order.TradeOrderDto;
import top.starshine.commons.entity.order.RefundRecord;
import top.starshine.commons.entity.order.TradeRefundDto;
import top.starshine.commons.model.mq.RabbitTopic;

/**
 * <h3>订单发送消息生产者</h3>
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
public class OrderSendMQ extends RabbitTopic {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 发送超时未支付自动取消订单消息
     * @param payload 载荷数据
     */
    public void sendAutoTLLCancelAnOrder(OrderDetail payload){
        this.rabbitTemplate.convertAndSend(EXCHANGE_ORDER, ROUTING_CANCEL_AN_ORDER_ORDER, payload, message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            // 设置消息的持久化策略
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            // 设置额外的参数,消息头的属性的对象
            messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, payload.getClass());
            return message;
        }, new CorrelationData(String.valueOf(payload.getId())));
    }

    /**
     * 发送订单生成的数据进行保存操作
     * @param payload 载荷数据
     */
    public void sendOrderGenerateSave(TradeOrderDto payload){
        this.rabbitTemplate.convertAndSend(EXCHANGE_ORDER, ROUTING_ORDER_GENERATE_SAVE, payload, message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            // 设置消息的持久化策略
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            // 设置额外的参数,消息头的属性的对象
            messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, payload.getClass());
            return message;
        }, new CorrelationData(String.valueOf(payload.getOrderDetail().getId())));
    }

    /**
     * 发送 买家退款了，卖家要处理退款处理
     * @param payload 载荷数据
     */
    public void sendNoticeSellerHandleRefund(RefundRecord payload){
        this.rabbitTemplate.convertAndSend(EXCHANGE_ORDER, ROUTING_NOTICE_SELLER_HANDLE_REFUND, payload, message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            // 设置消息的持久化策略
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            // 设置额外的参数,消息头的属性的对象
            messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, payload.getClass());
            return message;
        }, new CorrelationData(String.valueOf(payload.getId())));
    }

    /**
     * 发送 卖家同意退款了，系统执行退款操作
     * @param payload 载荷数据
     */
    public void sendSystemHandleRefund(TradeRefundDto payload){
        this.rabbitTemplate.convertAndSend(EXCHANGE_PAYMENT, ROUTING_SYSTEM_HANDLE_REFUND, payload, message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            // 设置消息的持久化策略
            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            // 设置额外的参数,消息头的属性的对象
            messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, payload.getClass());
            return message;
        }, new CorrelationData(payload.getOutTradeNo()));
    }

}
