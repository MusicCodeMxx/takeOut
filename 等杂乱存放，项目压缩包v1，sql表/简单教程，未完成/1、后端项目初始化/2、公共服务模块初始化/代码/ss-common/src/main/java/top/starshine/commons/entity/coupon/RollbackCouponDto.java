package top.starshine.commons.entity.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h3>回滚优惠券传输层对象</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/5  下午 5:16  周五
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RollbackCouponDto implements java.io.Serializable{

    /**用户主键*/
    private Long userId;

    /**订单编号*/
    private String outTradeNo;

}
