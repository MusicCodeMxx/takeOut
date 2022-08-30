package top.starshine.commons.entity.paymenty;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.starshine.commons.entity.BaseEntity;

import java.util.Date;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 1:49  周一
 * @Description: hello world
 */
@Data // set get tostring hash...
@AllArgsConstructor // 全参构造器
@NoArgsConstructor // 无参构造器
@Accessors(chain = true) // 构建模式
@TableName("ss_payment_record") // 用户表
@EqualsAndHashCode(callSuper = true) // 继承需要操作父类校验之类
public class PaymentRecord extends BaseEntity implements java.io.Serializable {

    /** 存根类型标题,0支付回调存根,1支付异步通知存根,2退款存根 */
    private String payTypeTitle;

    /** 订单金额。本次交易支付订单金额，单位为人民币（元），精确到小数点后 2 位 */
    private Double totalAmount;

    /** 实收金额。商家在交易中实际收到的款项，单位为人民币（元），精确到小数点后 2 位 */
    private Double receiptAmount;

    /** 开票金额。用户在交易中支付的可开发票的金额，单位为人民币（元），精确到小数点后 2 位 */
    private Double invoiceAmount;

    /** 用户在交易中支付的金额，单位为人民币（元），精确到小数点后 2 位 */
    private Double buyerPayAmount;

    /** 使用集分宝支付金额，单位为人民币（元），精确到小数点后 2 位 */
    private Double pointAmount;

    /** 总退款金额。退款通知中，返回总退款金额，单位为人民币（元），精确到小数点后 2 位*/
    private Double refundFee;

    /** 字符编码格式 */
    private String charset;

    /** 服务名,接口名 */
    private String method;

    /**签名,详情可查看,异步返回结果的验签*/
    private String sign;

    /** 授权方的APPID。由于本接口暂不开放第三方应用授权，因此 auth_app_id=app_id */
    private String authAppId;

    /**支付应用版本*/
    private String version;

    /**加密类型,签名类型*/
    private String signType;

    /**支付宝交易号，支付宝交易凭证号*/
    private String tradeNo;

    /**支付宝应用的APPID。支付宝分配给开发者的应用 ID*/
    private String appId;

    /**商家订单号。原支付请求的商家订单号*/
    private String outTradeNo;

    /**商家业务号。商家业务ID，通常是退款通知中返回的退款申请流水号*/
    private String outBizNo;

    /**买家支付宝账号 ID。以 2088 开头的纯 16 位数字*/
    private String buyerId;

    /**卖家支付宝账号 ID。以 2088 开头的纯 16 位数字*/
    private String sellerId;

    /**交易状态。交易目前所处状态，详情可查看下表 交易状态说明*/
    private String tradeStatus;

    /**通知类型*/
    private String notifyType;

    /**通知校验 ID*/
    private String notifyId;

    /**订单标题/商品标题/交易标题/订单关键字等，是请求时对应参数，会在通知中原样传回*/
    private String subject;

    /**商品描述。该订单的备注、描述、明细等。对应请求时的 body 参数，会在通知中原样传回*/
    private String body;

    /**支付金额信息。支付成功的各个渠道金额信息。详情可查看下文 资金明细信息说明*/
    private String fundBillList;

    /**优惠券信息。本交易支付时所使用的所有优惠券信息。详情可查看下表 优惠券信息说明*/
    private String vocherDetailList;

    /**回传参数，公共回传参数，如果请求时传递了该参数，则返回的异步通知会原样传回。本参数必须进行 UrlEncode 之后才可传入*/
    private String passbackParams;

    /**交易创建时间*/
    private Date gmtCreate;

    /**交易付款时间*/
    private Date gmtPayment;

    /**交易退款时间*/
    private Date gmtRefund;

    /**交易结束时间*/
    private Date gmtClose;

    /**支付时间*/
    private Date timestamp;

    /**通知的发送时间*/
    private Date notifyTime;

}
