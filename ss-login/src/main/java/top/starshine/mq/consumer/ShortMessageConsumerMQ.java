package top.starshine.mq.consumer;


import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import top.starshine.commons.fn.mq.RabbitRetryConsumptionCallback;
import top.starshine.commons.model.mq.RabbitTopic;
import top.starshine.commons.model.redis.CachePrefix;
import top.starshine.commons.util.RandomNumberUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * <h3>短信消费者</h3>
 * @author: starshine
 * @version: 1.0
 * @since: 2022/7/7  下午 9:27  周四
 * @Description:
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ShortMessageConsumerMQ extends RabbitTopic {

    private final RedisTemplate redisTemplate;

    /**
     * <h3>发送消息</h3>
     * @param payload {@link String} 消息载荷,消息数据体
     * @param message {@link Message} 消息实例
     * @param channel {@link Channel} 信道实例
     */
    @RabbitListener(queues = {QUEUE_SHORT_MESSAGE}, containerFactory = "simpleTopicOrderDeadContainerManualACK")
    public void saveTradeDetailsInfoRender(@Payload String payload, Message message, Channel channel) throws IOException {
        try {
            Integer validateCode = RandomNumberUtils.generateValidateCode(6);

            log.info("验证码：{}",validateCode);

            // ... 生成验证码, 存入缓存
            redisTemplate.opsForValue().set(CachePrefix.LOGIN_VERIFICATION_CODE + payload,
                                            validateCode,
                                            3,
                                            TimeUnit.MINUTES);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);// 手动应答 or 重试处理
        } catch (Exception ex) {
            // 重试指定次数执行回调函数
            RabbitRetryConsumptionCallback.RunCallback.run(message,channel,()->{
                // ... 这里保存异常信息
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);// 手动 ACK
            });
            ex.printStackTrace();
        }
    }

}

