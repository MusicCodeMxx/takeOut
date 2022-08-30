package top.starshine.service;

import top.starshine.commons.entity.order.TradeOrderDto;
import top.starshine.commons.model.renderstep.RenderStep;
import top.starshine.commons.model.renderstep.RenderStepEnums;
import top.starshine.commons.util.CollectionUtils;

import java.util.List;

/**
 * <h3>订单交易构造和创建服务接口</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 5:33  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
public interface TradeBuilderService {

    /**
     * <h3>将购物车产品集合创建一笔交易</h3>
     * <ul>
     *    <li>进行创建</li>
     *    <li>进行构建</li>
     *    <li>进行渲染细节</li>
     * </ul>
     * @param tod {@link TradeOrderDto} 交易订单数据对象
     * @return 订单主键
     */
    String createTrade(TradeOrderDto tod);

    /**
     * <h3>根据渲染步骤，渲染购物车信息</h3>
     * @param tod {@link TradeOrderDto} 交易 DTO
     * @param defaultRender {@link RenderStepEnums} 渲染枚举顺序执行的数组
     * @param renderSteps 步骤实现类集合
     */
    default void renderCartBySteps(TradeOrderDto tod, RenderStepEnums[] defaultRender, List<RenderStep> renderSteps) {
        if (null == defaultRender) return;
        if (CollectionUtils.isEmpty(renderSteps)) return;
        // 循环指定的渲染步骤
        for (RenderStepEnums step : defaultRender) {
            // 循环渲染步骤的实现类列表
            for (RenderStep render : renderSteps) {
                // 根据渲染绑定的渲染步骤进行渲染
                if (render.step(step)) render.render(tod);
            }
        }
    }

}
