package top.starshine.commons.entity.order;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <h3>交易退款数据层对象</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/5  下午 2:00  周五
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Setter
@Getter
@Accessors(chain = true) // 构建模式
public class TradeRefundDto implements java.io.Serializable{

    /**原订单ID*/
    private Long orderId;

    /** 绑定用户主键 */
    private Long userId;

    /**支付宝交易号，支付宝交易凭证号*/
    private String tradeNo;

    /**订单号*/
    private String outTradeNo;

    /**订单金额。本次交易支付订单金额，单位为人民币（元），精确到小数点后 2 位*/
    private Double price;

    /** 退款备注: 买家申请退款，卖家同意退款处理 */
    private String refundReason;

    /**退款编号*/
    private Long refundId;

    public TradeRefundDto() {
    }

    public TradeRefundDto(RefundRecord tradeRefund) {
        this.orderId =  tradeRefund.getOrderId();
        this.userId =  tradeRefund.getUserId();
        this.outTradeNo =  tradeRefund.getOutTradeNo();
        this.refundId =  tradeRefund.getId();
        this.price =  tradeRefund.getPrice();
    }
}
