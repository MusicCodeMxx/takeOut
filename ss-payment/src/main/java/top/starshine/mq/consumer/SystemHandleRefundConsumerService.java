package top.starshine.mq.consumer;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import top.starshine.commons.entity.coupon.Coupon;
import top.starshine.commons.entity.coupon.RollbackCouponDto;
import top.starshine.commons.entity.order.TradeRefundDto;
import top.starshine.commons.fn.mq.RabbitRetryConsumptionCallback;
import top.starshine.commons.model.mq.RabbitTopic;
import top.starshine.config.AlipayTemplate;
import top.starshine.mq.producer.PaymentSendMQ;
import top.starshine.service.TradeRefundRecordService;

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
public class SystemHandleRefundConsumerService extends RabbitTopic {


    private final PaymentSendMQ paymentSendMQ;
    private final AlipayTemplate alipayTemplate;
    private final TradeRefundRecordService tradeRefundRecordService;

    /**
     * <h3>手动确认机制(ACK)</h3>
     * 消费消息，卖家同意了买家的退款申请，系统执行退款操作
     * @param payload {@link String} 消息载荷,消息数据体
     * @param message {@link Message} 消息实例
     * @param channel {@link Channel} 信道实例
     */
    @RabbitListener(queues = {QUEUE_SYSTEM_HANDLE_REFUND}, containerFactory = "simpleTopicOrderDeadContainerManualACK")
    public void consumerOrder(@Payload TradeRefundDto payload, Message message, Channel channel) throws IOException {
        try {

            // tradeNo 支付宝交易号
            // outTradeNo 商家订单编号
            // refundAmount 退款金额
            // outRequestNo 退款编号
            // refundReason 退款备注

            log.warn(new ObjectMapper().writeValueAsString(payload));

            // 调用服务退款
            boolean b = systemRefundHandle(payload.getTradeNo(), payload.getOutTradeNo(),
                                                String.valueOf(payload.getPrice()),
                                                String.valueOf(payload.getRefundId()),
                                                payload.getRefundReason());

            // 发送消息给订单微服务，退款结果，成功还是失败都通知，
            // TODO 通知订单系统，通知卖家，通知买家。系统退款操作成功，失败就推到人工台

            // 将订单改为 系统退款成功


            // 有优惠券的修改状态
            paymentSendMQ.sendRollbackCouponUse(new RollbackCouponDto(payload.getUserId(), payload.getOutTradeNo()));

            log.warn("系统操作退款接口，执行结果=>{}",b);

            // 告诉服务器收到这条消息,已经被我消费了,可以在队列删掉,这样以后就不会再发了,否则消息服务器以为这条消息没处理掉,后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception ex) {
            ex.printStackTrace();
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
     * 系统退款操作
     * @param tradeNo 已支付的交易订单
     * @param outTradeNo 退款商家系统中的订单号(原支付时的订单号
     * @param refundAmount 退款金额
     * @param outRequestNo 订单退款主键
     * @param refundReason 备注
     */
    private boolean systemRefundHandle(String tradeNo, String outTradeNo, String refundAmount, String outRequestNo, String refundReason){
        AlipayTradeRefundResponse atrr = null;
        try {
            atrr = alipayTemplate.tradeRefund(tradeNo, outTradeNo, refundAmount, outRequestNo, refundReason);
            // 返回结果成功之后,检查资金变动
            if (atrr.isSuccess()) return atrr.getFundChange().equals("Y");
            // 返回结果成功,但是资金没有变动
            return false;
        } catch (AlipayApiException | JsonProcessingException e) {
            e.printStackTrace();
            return false;
        } finally {
            // 退款存根
            if (atrr != null) tradeRefundRecordService.saveSystemRefund(atrr.getBody(), atrr.getParams());
        }
    }

}
