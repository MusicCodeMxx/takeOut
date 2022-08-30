package top.starshine.commons.fn.mq;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

import java.io.IOException;
import java.util.Map;

/**
 * <h3>函数,兔子消息队列重试失败之后回调函数</h3>
 * @author: starshine
 * @version: 1.0
 * @since: 2022/7/22  下午 4:00  周五
 * @Description:
 */
public interface RabbitRetryConsumptionCallback {

    /**执行的回调函数*/
    void retryCallback() throws IOException;

    /**
     * <h3>执行函数,执行回调函数</h3>
     * @author: starshine
     * @version: 1.0
     * @since: 2022/7/22  下午 4:00  周五
     * @Description:
     */
    @Slf4j
    final class RunCallback{

        /**重试主键*/
        private static final String RETRY_KEY = "retry-count";
        /**重试次数*/
        private static final Integer RETRY_MAX = 3;

        /**私有化构造器*/
        private RunCallback(){}

        /**
         * <h3>具体函数执行</h3>
         * <p>重试多次之后执行回调函数</p>
         * @param message {@link Message} 消息实例
         * @param channel {@link Channel} 信道实例
         * @param rabbitRetryConsumptionCallback {@link RabbitRetryConsumptionCallback} 函数,回调函数
         * @throws IOException 异常
         */
        public static void run(Message message, Channel channel, RabbitRetryConsumptionCallback rabbitRetryConsumptionCallback) throws IOException {

            // 获取消息头里的数据
            Map<String, Object> headers = message.getMessageProperties().getHeaders();

            // 获取重试的次数
            Integer retryCount = headers.containsKey(RETRY_KEY) ? (Integer) headers.get(RETRY_KEY) : 0;

            // 重试三次之后走后备处理
            if (retryCount++ < RETRY_MAX) {
                log.warn("消费异常,已尝试重试{}次", retryCount);

                // 重新放回
                headers.put(RETRY_KEY, retryCount);

                // 拒绝消息应答
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

                // 重新把消息发送自己的交换机和队列中继续消费
                channel.basicPublish(
                        message.getMessageProperties().getReceivedExchange(),
                        message.getMessageProperties().getReceivedRoutingKey(),
                        new AMQP.BasicProperties.Builder().headers(headers).build(),
                        message.getBody());

                //捕获异常后，重新发送到指定队列，自动ack不抛出异常即为ack
                //channel.basicPublish(
                //        message.getMessageProperties().getReceivedExchange(),
                //        message.getMessageProperties().getReceivedRoutingKey(),
                //        MessageProperties.PERSISTENT_TEXT_PLAIN,
                //        message.getBody());


            } else {
                log.error("消费异常, 已尝试多次, 执行后备处理");

                // 回调函数执行,将消息存盘,通知人工
                rabbitRetryConsumptionCallback.retryCallback();

                // 手动 ack
                //channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

                // 投递死信队列
                //channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);

            }

            headers = null;
            retryCount = null;

        }
    }
}


