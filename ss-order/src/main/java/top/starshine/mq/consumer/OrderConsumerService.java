package top.starshine.mq.consumer;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import top.starshine.commons.entity.order.OrderDetail;
import top.starshine.commons.entity.order.RefundRecord;
import top.starshine.commons.entity.order.TradeOrderDto;
import top.starshine.commons.fn.mq.RabbitRetryConsumptionCallback;
import top.starshine.commons.model.mq.RabbitTopic;
import top.starshine.service.OrderDetailService;
import top.starshine.service.TradeRefundService;

import java.io.IOException;

/**
 * <h3>订单消息队列-消费者实现类</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/2  下午 11:16  周二
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConsumerService extends RabbitTopic {

    private final OrderDetailService orderDetailService;
    private final TradeRefundService tradeRefundService;

    /**
     * <h3>手动确认机制(ACK)</h3>
     * 消费超时未支付的订单自动取消
     * @param payload {@link String} 消息载荷,消息数据体
     * @param message {@link Message} 消息实例
     * @param channel {@link Channel} 信道实例
     */
    @RabbitListener(queues = {QUEUE_CONSUMER_CANCEL_AN_ORDER},
            containerFactory = "simpleTopicOrderDeadContainerManualACK")
    public void consumerOrder(@Payload OrderDetail payload, Message message, Channel channel) throws IOException {
        try {

            // 操作取消订单
            log.info("检查订单超时消息开始检查=>{}", payload.getId());
            if (orderDetailService.cancel(payload)) log.info("检查到该订单已超时并且关闭了{}该订单", payload.getId());


            // 告诉服务器收到这条消息,已经被我消费了,可以在队列删掉,这样以后就不会再发了,否则消息服务器以为这条消息没处理掉,后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception ex) {
            // 重试次数, 回调函数保存持久层, 通知人工
            RabbitRetryConsumptionCallback.RunCallback.run(message,channel,()->{
                // 手动 ack, 消费掉消息
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            });
        }finally {
            payload = null;
        }
    }

    /**
     * <h3>手动确认机制(ACK)</h3>
     * 消费订单生成的数据进行消费操作
     * @param payload {@link String} 消息载荷,消息数据体
     * @param message {@link Message} 消息实例
     * @param channel {@link Channel} 信道实例
     */
    @RabbitListener(queues = {QUEUE_ORDER_GENERATE_SAVE}, containerFactory = "simpleTopicOrderDeadContainerManualACK")
    public void consumerOrderGenerateSave(@Payload TradeOrderDto payload, Message message, Channel channel) throws IOException {
        try {
            // 保存生成的订单数据, 分布式事务保存订单数据
            orderDetailService.saveTradeBuildData(payload);
            // 告诉服务器收到这条消息,已经被我消费了,可以在队列删掉,这样以后就不会再发了,否则消息服务器以为这条消息没处理掉,后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception ex) {
            ex.printStackTrace();
            // 重试次数, 回调函数保存持久层, 通知人工
            RabbitRetryConsumptionCallback.RunCallback.run(message, channel, () -> {
                // 手动 ack, 消费掉消息
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            });
        } finally {
            payload = null;
        }
    }

    /**
     * <h3>手动确认机制(ACK)</h3>
     * 消费消息， 买家处理退款通知卖家，处理退款处理
     * @param payload {@link String} 消息载荷,消息数据体
     * @param message {@link Message} 消息实例
     * @param channel {@link Channel} 信道实例
     */
    @RabbitListener(queues = {QUEUE_NOTICE_SELLER_HANDLE_REFUND}, containerFactory = "simpleTopicOrderDeadContainerManualACK")
    public void consumerNoticeSellerHandleRefund(@Payload RefundRecord payload, Message message, Channel channel) throws IOException {
        try {

            /*
                这里通知卖家
                这里直接操作退款，admin 后台还没有搭建，后续处理
                调用退款服务，处理退款处理
             */
            tradeRefundService.confirmRefund(payload);

            // 告诉服务器收到这条消息,已经被我消费了,可以在队列删掉,这样以后就不会再发了,否则消息服务器以为这条消息没处理掉,后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception ex) {
            ex.printStackTrace();
            // 重试次数, 回调函数保存持久层, 通知人工
            RabbitRetryConsumptionCallback.RunCallback.run(message, channel, () -> {

                // 手动 ack, 消费掉消息
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            });
        } finally {
            payload = null;
        }
    }


}
