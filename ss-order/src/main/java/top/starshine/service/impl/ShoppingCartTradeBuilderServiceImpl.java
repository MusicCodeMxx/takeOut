package top.starshine.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.starshine.commons.entity.order.TradeOrderDto;
import top.starshine.commons.exception.InternalServerErrorException;
import top.starshine.commons.model.renderstep.RenderStep;
import top.starshine.commons.model.renderstep.RenderStepStatement;
import top.starshine.commons.status.OrderStatus;
import top.starshine.service.TradeBuilderService;

import java.util.List;

/**
 * <h3>购物车交易构造和创建</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 5:33  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Service
@RequiredArgsConstructor
public class ShoppingCartTradeBuilderServiceImpl implements TradeBuilderService {

    private final List<RenderStep> cartRenderSteps;

    /**
     * <h3>将购物车产品集合创建一笔交易</h3>
     * <ul>
     *    <li>进行创建</li>
     *    <li>进行构建</li>
     *    <li>进行渲染细节</li>
     * </ul>
     * @param tod {@link TradeOrderDto} 交易订单数据对象
     */
    @Override
    public String createTrade(TradeOrderDto tod) {
        renderCartBySteps(tod, RenderStepStatement.cartRender,cartRenderSteps);
        Long id = tod.getOrderDetail().getId();
        if (null != id) return String.valueOf(id);
        throw new InternalServerErrorException(OrderStatus.ERROR);
    }

}
