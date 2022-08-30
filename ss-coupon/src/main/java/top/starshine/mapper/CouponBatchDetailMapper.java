package top.starshine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.starshine.commons.entity.coupon.CouponBatchDetail;

import java.util.List;

/**
 * <h3>优惠券批次接口</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 2:53  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
public interface CouponBatchDetailMapper extends BaseMapper<CouponBatchDetail> {


    List<CouponBatchDetail> findList();

}
