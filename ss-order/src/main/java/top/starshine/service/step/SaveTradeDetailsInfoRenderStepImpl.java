package top.starshine.service.step;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.starshine.commons.entity.order.TradeOrderDto;
import top.starshine.commons.model.redis.CachePrefix;
import top.starshine.commons.model.renderstep.RenderStep;
import top.starshine.commons.model.renderstep.RenderStepEnums;
import top.starshine.mq.producer.OrderSendMQ;

/**
 * <h3>保存交易详情信息渲染步骤</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 5:11  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SaveTradeDetailsInfoRenderStepImpl implements RenderStep {

    private static final RenderStepEnums SAVE_TRADE_DETAILS_INFO = RenderStepEnums.SAVE_TRADE_DETAILS_INFO;

    private final OrderSendMQ orderSendMQ;
    private final RedisTemplate redisTemplate;

    /**
     * 检查是否相同渲染枚举
     * @return {@link RenderStepEnums} 渲染枚举
     */
    @Override
    public boolean step(RenderStepEnums rs) {
        return SAVE_TRADE_DETAILS_INFO.equals(rs);
    }

    @Async // 异步执行, 不阻塞等待完成
    @Override
    public void render(TradeOrderDto tod) {
        log.info("保存交易详情信息骤");

        // 保存到 redis缓存中, 不设置缓存过期
        redisTemplate.opsForValue().set(CachePrefix.RABBIT_MQ_ORDER_SAVE + tod.getOrderDetail().getId(), tod);

        // rabbit MQ 发送消息到队列中, 消费者收到消息进行消费, 使用 seata 进行分布式事务保存
        orderSendMQ.sendOrderGenerateSave(tod);

    }

}
