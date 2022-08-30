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
 * <h3>满减优惠券对象</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/31  下午 11:51  周日
 * @Description: hello world
 */
@Data // set get tostring hash...
@AllArgsConstructor // 全参构造器
@NoArgsConstructor // 无参构造器
@Accessors(chain = true) // 构建模式
@TableName("ss_coupon") // 用户表
@EqualsAndHashCode(callSuper = true) // 继承需要操作父类校验之类
public class Coupon extends BaseEntity implements java.io.Serializable {

    /**批次表主键*/
    private Long batchId;

    /**优惠券面额*/
    private Double price;

    /**消费门槛*/
    private Double threshold;

    /**批次表状态: 0未使用, 1已使用, 2过期, 3冻结, 4异常*/
    private Integer status;

    /**优惠券名称*/
    private String couponName;

    /**优惠券描述*/
    private String description;

    /**使用起始时间*/
    private Date startTime;

    /**使用截止时间*/
    private Date endTime;

    /**领取该券的用户主键*/
    private Long userId;

    /**优惠券被核销(使用)时间*/
    private Date useTime;

    /**核销(使用)该优惠券订单号 */
    private String useOutTradeNo;

}
