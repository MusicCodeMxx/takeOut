package top.starshine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.starshine.commons.entity.coupon.Coupon;
import top.starshine.commons.entity.coupon.CouponBatchDetail;

import java.util.List;

/**
 * <h3>优惠券服务接口</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 2:52  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
public interface CouponService extends IService<Coupon> {

    /**
     * 获取当前登录用户的优惠券列表
     * @return 优惠券列表
     */
    List<Coupon> meCouponList();

    /**
     * 异步保存添加优惠券
     * @param couponBatchDetail 优惠券批次对象
     */
    Long addCoupon(CouponBatchDetail couponBatchDetail);


    List<Coupon> selectIdAndBatchId();
}
