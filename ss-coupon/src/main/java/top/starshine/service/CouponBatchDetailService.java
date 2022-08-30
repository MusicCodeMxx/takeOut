package top.starshine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.starshine.commons.entity.coupon.CouponBatchDetail;

import java.util.List;

/**
 * <h3>优惠券批次服务接口</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 2:56  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
public interface CouponBatchDetailService extends IService<CouponBatchDetail> {

    /**
     * 领取指定的优惠券
     * @param id 批次主键
     */
    Long receiveCouponById(Long id);

    List<CouponBatchDetail> findList();

}
