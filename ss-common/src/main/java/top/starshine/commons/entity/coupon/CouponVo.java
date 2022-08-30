package top.starshine.commons.entity.coupon;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 2:19  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Setter
@Getter
public class CouponVo implements java.io.Serializable{

    /**优惠券主键*/
    private String id;

    /**批次表主键*/
    private String batchId;

    /**优惠券面额*/
    private Double price;

    /**消费门槛*/
    private Double threshold;

    /**优惠券名称*/
    private String couponName;

    /**优惠券描述*/
    private String description;

    /**使用起始时间*/
    private Date startTime;

    /**使用截止时间*/
    private Date endTime;

}
