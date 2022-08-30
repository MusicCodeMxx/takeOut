package top.starshine.commons.entity.order;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.starshine.commons.entity.BaseEntity;

/**
 * <h3>交易退款</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/4  下午 7:56  周四
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Data // set get tostring hash...
@AllArgsConstructor // 全参构造器
@NoArgsConstructor // 无参构造器
@Accessors(chain = true) // 构建模式
@TableName("ss_refund_record") // 用户表
@EqualsAndHashCode(callSuper = true) // 继承需要操作父类校验之类
public class RefundRecord extends BaseEntity implements java.io.Serializable {

    /**订单ID*/
    private Long orderId;

    /** 绑定用户主键 */
    private Long userId;

    /**订单号*/
    private String outTradeNo;

    /** 买家名称 */
    private String buyerName;

    /**买家联系电话 */
    private String buyerPhoneNumber;

    /**买家申请内容 */
    private String buyerReason;

    /**退款申请状态,0买家申请提交,1卖家处理,2系统退款,3完成,4系统退款异常,5卖拒绝退款,6买家取消退款*/
    private Integer status;

    /**卖家回复内容 */
    private String handleReply;

    /**订单金额。本次交易支付订单金额，单位为人民币（元），精确到小数点后 2 位*/
    private Double price;

    /**退款类型:0系统自动退款, 1用户发起退款, 2商家发起退款*/
    private Integer type;

}
