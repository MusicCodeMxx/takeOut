package top.starshine.commons.entity.order;

import lombok.Getter;
import lombok.Setter;
import top.starshine.commons.model.validation.IsPhoneNumber;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <h3>前端提交订单检查对象</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 5:25  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Setter
@Getter
public class SubmitOrderVC implements java.io.Serializable {

    // ============ 配送信息

    /** 收货人姓名 */
    @NotEmpty(message = "收货人不能为空")
    private String consigneeName;

    /** 手机号 */
    @IsPhoneNumber
    @NotEmpty(message = "手机号不能为空")
    private String phoneNumber;

    /** 地址绑定的主键 */
    @NotNull(message = "地址绑定标识不能为空")
    private Long addressBookId;

    /** 详细地址 */
    @NotEmpty(message = "配送地址不能为空")
    private String detail;

    /** 性别 0 女 1 男 */
    private Integer sex;

    /** 标签,0默认,1家,2学校,3公司,4其他*/
    private String label;

    /** 用户备注 */
    private String remark;

    // ============= 订单价格

    /**订单原始价*/
    @NotNull(message = "订单总价格不能为空")
    private Double originalPrice;

    /**运费*/
    private Double freightPrice;

    /** 该订单产品总数 */
    @NotNull(message = "购买数量不能为空")
    private Integer productTotalNumber;

    /** 优惠之后总价,实际支付的金额 */
    @NotEmpty(message = "实际支付价格不能为空")
    private String billPrice;

    // ============ 优惠券信息

    /**优惠券主键*/
    private Long couponId;

    /**优惠要减免总价*/
    private Double couponPrice;

}
