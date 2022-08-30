package top.starshine.commons.dubbo;

import top.starshine.commons.entity.coupon.Coupon;
import top.starshine.commons.entity.coupon.CouponVo;

import java.util.List;

/**
 * <h3>优惠服务 Dubbo 远程调用服务</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/31  下午 11:49  周日
 * @Description: hello world
 */
public interface CouponDubboService {

    /**
     * 获取用户当前持有的优惠券列表
     * @return 优惠券视图对象
     */
    List<CouponVo> getUserCouponList();

    /**
     * 获取用户当前持有的优惠券列表
     * @return 优惠券对象(数据库映射对象)
     */
    List<Coupon> getUserCoupons();

    /**
     * 更新使用优惠券
     * @param coupon 优惠券对象
     */
    void updateUseCoupon(Coupon coupon);
}
