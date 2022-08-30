package top.starshine.commons.model.renderstep;

import top.starshine.commons.entity.order.TradeOrderDto;

/**
 * <h3>购物车渲染接口</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 5:09  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
public interface RenderStep {

    /**
     * 渲染指定的业务的步骤实现
     * @param renderStepEnums {@link RenderStepEnums } 渲染枚举枚举
     * @return {@link boolean } true相同, false不相同
     */
    boolean step(RenderStepEnums renderStepEnums);

    /**
     * 渲染指定步骤的功能处理实现
     * @param tod {@link TradeOrderDto} 订单交易数据对象
     */
    void render(TradeOrderDto tod);

}
