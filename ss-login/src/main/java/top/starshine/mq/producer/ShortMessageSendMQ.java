package top.starshine.mq.producer;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.stereotype.Service;
import top.starshine.commons.model.mq.RabbitTopic;

/**
 * <h3>短信消息生产者</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/7  下午 9:13  周四
 * @Description: hello world
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShortMessageSendMQ extends RabbitTopic {

    private final RabbitTemplate rabbitTemplate;

    /** 发送短信的消息 */
    public void sendMsm(String source) {
        // 生成消息主键,可以在发送失败情况下,将消息写入到数据库
        //Long messageQueueId = new DefaultIdentifierGenerator().nextId(new Object());
        this.rabbitTemplate.convertAndSend(EXCHANGE_SHORT_MESSAGE, ROUTING_SHORT_MESSAGE, source, message -> {
                    MessageProperties messageProperties = message.getMessageProperties();
                    // 设置消息的持久化策略
                    messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    // 设置额外的参数,消息头的属性的对象
                    messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, source.getClass());
                    return message;
        }, new CorrelationData(String.valueOf(new DefaultIdentifierGenerator().nextId(new Object()))));
    }

}
