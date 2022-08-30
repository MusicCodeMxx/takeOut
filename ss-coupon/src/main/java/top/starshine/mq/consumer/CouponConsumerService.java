package top.starshine.mq.consumer;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import top.starshine.commons.entity.coupon.Coupon;
import top.starshine.commons.entity.coupon.RollbackCouponDto;
import top.starshine.commons.entity.order.OrderDetail;
import top.starshine.commons.entity.order.RefundRecord;
import top.starshine.commons.entity.order.TradeOrderDto;
import top.starshine.commons.fn.mq.RabbitRetryConsumptionCallback;
import top.starshine.commons.model.mq.RabbitTopic;
import top.starshine.service.CouponService;

import java.io.IOException;

/**
 * <h3>回滚优惠券使用消息队列-消费者实现类</h3>
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
public class CouponConsumerService extends RabbitTopic {

    private final CouponService couponService;

    /**
     * <h3>手动确认机制(ACK)</h3>
     * 消费消息， 回滚用户订单中使用的优惠券至未使用状态
     * @param payload {@link Object} 消息载荷,消息数据体
     * @param message {@link Message} 消息实例
     * @param channel {@link Channel} 信道实例
     */
    @RabbitListener(queues = {QUEUE_ROLLBACK_COUPON_USE}, containerFactory = "simpleTopicOrderDeadContainerManualACK")
    public void consumerRollbackCouponUse(@Payload RollbackCouponDto payload, Message message, Channel channel) throws IOException {
        try {

            // 查询状态
            Coupon one = new LambdaQueryChainWrapper<>(couponService.getBaseMapper())
                    .eq(Coupon::getIsDelete, 0)
                    .eq(Coupon::getUseOutTradeNo, payload.getOutTradeNo())
                    .select(Coupon::getId, Coupon::getBatchId)
                    .one();

            // 检查是否有相同的优惠券，同优惠券不同同时持有两张
            Integer count = new LambdaQueryChainWrapper<>(couponService.getBaseMapper())
                    .eq(Coupon::getBatchId, one.getBatchId())
                    .eq(Coupon::getUserId, payload.getUserId())
                    .eq(Coupon::getIsDelete, 0)
                    .eq(Coupon::getStatus, 0)
                    .count();

            // 持有同批次的优惠券不执行退券
            if (null == count || count ==  0) {
                // 符合条件就进行将状态 改为未使用，清除使用 时间，订单号
                new LambdaUpdateChainWrapper<>(couponService.getBaseMapper())
                        .set(Coupon::getStatus, 0)
                        .set(Coupon::getUseOutTradeNo, "")
                        .set(Coupon::getUseTime, null)
                        .eq(Coupon::getId,one.getId())
                        .update();
            }

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
