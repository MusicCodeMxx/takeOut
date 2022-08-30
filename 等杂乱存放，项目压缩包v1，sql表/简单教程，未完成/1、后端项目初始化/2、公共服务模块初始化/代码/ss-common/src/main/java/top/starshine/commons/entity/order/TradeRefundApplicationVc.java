package top.starshine.commons.entity.order;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <h3>交易退款申请检查对象</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/4  下午 9:56  周四
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Setter
@Getter
public class TradeRefundApplicationVc implements java.io.Serializable{

    @NotNull(message = "订单标识不能为空")
    private Long orderId;

    /** 退款申请人 */
    @NotEmpty(message = "退款申请人姓名不能为空")
    private String buyerName;

    /** 退款申请人的联系电话 */
    @NotEmpty(message = "退款人手机号不能为空")
    private String buyerPhoneNumber;

    @NotEmpty(message = "退款原因不能为空")
    private String buyerReason;

}
