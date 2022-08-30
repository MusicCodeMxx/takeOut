package top.starshine.commons.entity.coupon;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.starshine.commons.entity.BaseEntity;

import java.util.Date;

/**
 * <h3>优惠券批次信息</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  上午 12:02  周一
 * @Description: hello world
 */
@Data // set get tostring hash...
@AllArgsConstructor // 全参构造器
@NoArgsConstructor // 无参构造器
@Accessors(chain = true) // 构建模式
@TableName("ss_coupon_batch_detail") // 用户表
@EqualsAndHashCode(callSuper = true) // 继承需要操作父类校验之类
public class CouponBatchDetail extends BaseEntity implements java.io.Serializable {

    /**优惠券名称*/
    private String couponName;

    /**优惠券批次名称*/
    private String batchName;

    /**优惠券是的描述*/
    private String description;

    /**批次表状态:0未发布, 1发放中, 2领取完, 3过期, 4冻结, 5异常*/
    private Integer status;

    /**优惠券面额*/
    private Double price;

    /**消费门槛*/
    private Double threshold;

    /**优惠券总量*/
    private Integer totalCount;

    /**优惠券已领取量*/
    private Integer receiveCount;

    /**优惠券有效期开始日期*/
    private Date startTime;

    /**优惠券失效开始日期*/
    private Date endTime;

}
