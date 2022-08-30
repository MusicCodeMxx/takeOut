package top.starshine.service.step;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.starshine.commons.entity.order.OrderDetail;
import top.starshine.commons.entity.order.TradeOrderDto;
import top.starshine.commons.exception.InternalServerErrorException;
import top.starshine.commons.model.renderstep.RenderStep;
import top.starshine.commons.model.renderstep.RenderStepEnums;
import top.starshine.commons.status.OrderStatus;
import top.starshine.commons.util.CurrencyUtil;

/**
 * <h3>计算账单价格渲染步骤</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 5:11  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Slf4j
@Service
public class ComputedBillPriceRenderStepImpl implements RenderStep {

    private static final RenderStepEnums COMPUTED_BILL_PRICE = RenderStepEnums.COMPUTED_BILL_PRICE;

    /**
     * 检查是否相同渲染枚举
     * @return {@link RenderStepEnums} 渲染枚举
     */
    @Override
    public boolean step(RenderStepEnums rs) {
        return COMPUTED_BILL_PRICE.equals(rs);
    }

    @Override
    public void render(TradeOrderDto tod) {
        log.info("计算账单价格渲染步骤");
        // 满减优惠不包含 运费, 运费另外计

        // 订单对象
        OrderDetail orderDetail = tod.getOrderDetail();
        // 订单原始总价
        Double originalPrice = orderDetail.getOriginalPrice();
        // 优惠面额
        Double price = orderDetail.getCouponPrice();

        // 检查是否有优惠面额
        if (null != price) {
            // 检查当前优惠券名额是否超过原价
            if (CurrencyUtil.gt(price,originalPrice))
                throw new InternalServerErrorException(OrderStatus.COUPON_PRICE_GT_ORIGINAL_PRICE);

            // 当前原价减掉优惠券面额得出实际支付价格
            orderDetail.setBillPrice(CurrencyUtil.subtract(originalPrice, price));
        }else {
            // 如果没有优惠券就直接回填原价
            orderDetail.setBillPrice(originalPrice);
        }

        // 检查是否有运费
        Double freightPrice = orderDetail.getFreightPrice();
        if (CurrencyUtil.notEqual(freightPrice,0.00D)){
            // 当前价格加上运费
            orderDetail.setBillPrice(CurrencyUtil.add(freightPrice, orderDetail.getBillPrice()));
        }
    }

}
