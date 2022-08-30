package top.starshine.service.step;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.starshine.commons.entity.coupon.Coupon;
import top.starshine.commons.entity.order.OrderDetail;
import top.starshine.commons.entity.order.TradeOrderDto;
import top.starshine.commons.exception.BadRequestException;
import top.starshine.commons.exception.InternalServerErrorException;
import top.starshine.commons.model.renderstep.RenderStep;
import top.starshine.commons.model.renderstep.RenderStepEnums;
import top.starshine.commons.status.CouponStatus;
import top.starshine.commons.util.CollectionUtils;
import top.starshine.commons.util.CurrencyUtil;

import java.util.Date;

/**
 * <h3>满减优惠券计算或全额折扣计数渲染步骤</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 5:11  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Slf4j
@Service
public class FullDiscountStepImpl implements RenderStep {

    private static final RenderStepEnums FULL_DISCOUNT = RenderStepEnums.FULL_DISCOUNT;

    /**
     * 检查是否相同渲染枚举
     * @return {@link RenderStepEnums} 渲染枚举
     */
    @Override
    public boolean step(RenderStepEnums rs) {
        return FULL_DISCOUNT.equals(rs);
    }

    @Override
    public void render(TradeOrderDto tod) {
        log.info("满减校验与规则计算");
        // 满减优惠不包含 运费, 运费另外计

        // 检查是否有优惠券
        if(CollectionUtils.isEmpty(tod.getCoupons())) return;

        OrderDetail orderDetail = tod.getOrderDetail();// 主订单对象
        Double originalPrice = orderDetail.getOriginalPrice();// 订单原始总价
        Long couponId = tod.getSubmitOrderVC().getCouponId();// 前端传入的优惠券主键
        Coupon coupon = null;// 优惠券对象

        // 检查前端是否有传入优惠券信息
        if (null == couponId){
            // 循环比较
            for (Coupon temp : tod.getCoupons()) {
                // 当前原始总价是否满足优惠券门槛
                if (CurrencyUtil.ge(originalPrice, temp.getThreshold())) {
                    // 对于第一个符合条件的优惠券存入
                    if (null == coupon) {
                        coupon = temp;
                        continue;
                    }
                    // 对于第一个之后的优惠券进行比较面额, 当前循环对象面额大于前一个优惠券对象, 就进行交互
                    if (CurrencyUtil.ge(temp.getThreshold(), coupon.getPrice()))  coupon = temp;
                }
            }
        }else {
            // 针对前端是否有传入优惠券选择
            // 检查优惠券是否真实有效的
            for (Coupon temp : tod.getCoupons()) {
                // 对比主键是否一致
                if (temp.getId().equals(couponId)) {
                    // 检查优惠券面额是否一致
                    if (CurrencyUtil.notEqual(temp.getPrice(),tod.getSubmitOrderVC().getCouponPrice()))
                        throw new BadRequestException(CouponStatus.THE_SELECTED_COUPON_PRICE_DOES_NOT_MATCH);
                    // 检查是否符合门槛
                    if (CurrencyUtil.gt(temp.getThreshold(), originalPrice))
                        throw new InternalServerErrorException(CouponStatus.THRESHOLD_NOT_REACHED);
                    // 取出核销使用
                    coupon = temp;
                }
            }
        }

        // 检查是否有符合的优惠券
        if (null == coupon) return;

        // 核销优惠券
        coupon.setStatus(1)
                .setUseTime(new Date())
                .setUseOutTradeNo(orderDetail.getOutTradeNo());
        // 存入
        tod.setCoupon(coupon);
        // 移除用户持有的优惠券列表
        tod.setCoupons(null);
        // 回填优惠券面额到订单
        orderDetail.setCouponPrice(coupon.getPrice());
    }

}
