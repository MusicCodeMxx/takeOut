package top.starshine.service.step;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.starshine.commons.entity.order.ChildOrderDetail;
import top.starshine.commons.entity.order.TradeOrderDto;
import top.starshine.commons.exception.InternalServerErrorException;
import top.starshine.commons.model.renderstep.RenderStep;
import top.starshine.commons.model.renderstep.RenderStepEnums;
import top.starshine.commons.status.OrderStatus;
import top.starshine.commons.util.CurrencyUtil;

import java.math.BigDecimal;

/**
 * <h3>计数子订单的总价(小计)渲染步骤</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 5:11  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Slf4j
@Service
public class ComputedChildOrderBillPriceRenderStepImpl implements RenderStep {

    private static final RenderStepEnums COMPUTED_CHILD_ORDER_BILL_PRICE = RenderStepEnums.COMPUTED_CHILD_ORDER_BILL_PRICE;

    /**
     * 检查是否相同渲染枚举
     * @return {@link RenderStepEnums} 渲染枚举
     */
    @Override
    public boolean step(RenderStepEnums rs) {
        return COMPUTED_CHILD_ORDER_BILL_PRICE.equals(rs);
    }

    @Override
    public void render(TradeOrderDto tod) {
        log.info("计算出所有产品小计价格步骤");

        BigDecimal totalAmount = new BigDecimal("0");// 计算出总价格
        Integer totalNumber = 0;// 计算出该订单有多少商品

        // 计算产品价格总价,优惠减免总价,优惠和总价相减的实付价格
        for (ChildOrderDetail cod : tod.getChildOrderDetails()) {
            // 计算数量
            Integer amount = cod.getAmount();
            totalNumber += amount;

            // 计算出总价 价格乘以数量
            BigDecimal priceBig = BigDecimal.valueOf(cod.getPrice()).multiply(new BigDecimal(amount));
            cod.setOriginalPrice(priceBig.doubleValue());
            totalAmount = totalAmount.add(priceBig);
        }

        // 检查原价
        if (CurrencyUtil.notEqual(totalAmount.doubleValue(),tod.getSubmitOrderVC().getOriginalPrice()))
            throw new InternalServerErrorException(OrderStatus.ORIGINAL_PRICE_DOES_NOT_MATCH);

        // 检查产品数量
        if (totalNumber.equals(tod.getSubmitOrderVC().getProductTotalNumber()))
            log.warn("产品数量不一致");
        if (tod.getSubmitOrderVC().getFreightPrice() > totalNumber)
            log.warn("产品数量大于当前运算的数量");

        // 回填购物车产品总价, 该笔订单有多个产品数量
        tod.getOrderDetail().setOriginalPrice(totalAmount.doubleValue()).setProductTotalNumber(totalNumber);
    }

}
