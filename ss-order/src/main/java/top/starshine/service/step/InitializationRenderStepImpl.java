package top.starshine.service.step;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.starshine.commons.entity.order.TradeOrderDto;
import top.starshine.commons.handle.ThreadLocalCache;
import top.starshine.commons.model.renderstep.RenderStep;
import top.starshine.commons.model.renderstep.RenderStepEnums;

/**
 * <h3>初始化渲染步骤</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 5:11  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Slf4j
@Service
public class InitializationRenderStepImpl implements RenderStep {

    private static final RenderStepEnums INITIALIZATION = RenderStepEnums.INITIALIZATION;

    /**
     * 检查是否相同渲染枚举
     * @return {@link RenderStepEnums} 渲染枚举
     */
    @Override
    public boolean step(RenderStepEnums rs) {
        return INITIALIZATION.equals(rs);
    }

    @Override
    public void render(TradeOrderDto tod) {
        tod.setUser(ThreadLocalCache.getNotNull());
        tod.init();
        log.info("订单初始化完成");
    }

}
