package top.starshine.service.step;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.starshine.commons.entity.order.OrderDetail;
import top.starshine.commons.entity.order.TradeOrderDto;
import top.starshine.commons.exception.BadRequestException;
import top.starshine.commons.model.renderstep.RenderStep;
import top.starshine.commons.model.renderstep.RenderStepEnums;
import top.starshine.commons.status.OrderStatus;
import top.starshine.commons.util.CurrencyUtil;

/**
 * <h3>计算运费价格渲染步骤</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 5:11  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Slf4j
@Service
public class ComputedFreightPriceRenderStepImpl implements RenderStep {

    private static final RenderStepEnums COMPUTED_FREIGHT_PRICE = RenderStepEnums.COMPUTED_FREIGHT_PRICE;

    /**
     * 检查是否相同渲染枚举
     * @return {@link RenderStepEnums} 渲染枚举
     */
    @Override
    public boolean step(RenderStepEnums rs) {
        return COMPUTED_FREIGHT_PRICE.equals(rs);
    }

    @Override
    public void render(TradeOrderDto tod) {
        log.info("计算运费价格步骤");

        OrderDetail orderDetail = tod.getOrderDetail();

        // 订单满 66 即可免运费
        if (CurrencyUtil.ge(orderDetail.getOriginalPrice(), 66.00D)) {
            orderDetail.setFreightPrice(0D);
        }else{
            orderDetail.setFreightPrice(8D);
        }

        // 检查前端传入的运费是否一致
        if (CurrencyUtil.notEqual(orderDetail.getFreightPrice(),tod.getSubmitOrderVC().getFreightPrice()))
            throw new BadRequestException(OrderStatus.FREIGHT_PRICE_DOES_NOT_MATCH);

    }

}
