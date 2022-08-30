package top.starshine.commons.entity.paymenty;

import lombok.Getter;
import lombok.Setter;

/**
 * <h3>支付宝支付成功回调通知</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 1:51  周一
 * @Description: hello world
 */
@Setter
@Getter
public class AlipayNotifyVc implements java.io.Serializable{

    /** (Number)订单金额。本次交易支付订单金额，单位为人民币（元），精确到小数点后 2 位 */
    private String total_amount;

    /** (Number)实收金额。商家在交易中实际收到的款项，单位为人民币（元），精确到小数点后 2 位 */
    private String receipt_amount;

    /** (Number)开票金额。用户在交易中支付的可开发票的金额，单位为人民币（元），精确到小数点后 2 位 */
    private String invoice_amount;

    /** (Number)用户在交易中支付的金额，单位为人民币（元），精确到小数点后 2 位 */
    private String buyer_pay_amount;

    /** (Number)使用集分宝支付金额，单位为人民币（元），精确到小数点后 2 位*/
    private String point_amount;

    /** (Number)总退款金额。退款通知中，返回总退款金额，单位为人民币（元），精确到小数点后 2 位 */
    private String refund_fee;

    /** (String)字符编码格式 */
    private String charset;

    /** (String)服务名,接口名 */
    private String method;

    /** (String)签名,详情可查看,异步返回结果的验签 */
    private String sign;

    /** (String)授权方的APPID。由于本接口暂不开放第三方应用授权，因此 auth_app_id=app_id */
    private String auth_app_id;

    /** (String)支付应用版本 */
    private String version;

    /** (String)加密类型,签名类型 */
    private String sign_type;

    /** (String)支付宝交易号，支付宝交易凭证号 */
    private String trade_no;

    /** (String)支付宝应用的APPID。支付宝分配给开发者的应用 ID */
    private String app_id;

    /** (String)商家订单号。原支付请求的商家订单号 */
    private String out_trade_no;

    /** (String)商家业务号。商家业务ID，通常是退款通知中返回的退款申请流水号 */
    private String out_biz_no;

    /** (String)买家支付宝账号 ID。以 2088 开头的纯 16 位数字 */
    private String buyer_id;

    /** (String)卖家支付宝账号 ID。以 2088 开头的纯 16 位数字 */
    private String seller_id;

    /** (String)交易状态。交易目前所处状态，详情可查看下表 交易状态说明 */
    private String trade_status;

    /** (String)通知类型 */
    private String notify_type;

    /** (String)通知校验 ID */
    private String notify_id;

    /** (String)订单标题/商品标题/交易标题/订单关键字等，是请求时对应参数，会在通知中原样传回 */
    private String subject;

    /** (String)商品描述。该订单的备注、描述、明细等。对应请求时的 body 参数，会在通知中原样传回 */
    private String body;

    /** (String)支付金额信息。支付成功的各个渠道金额信息。详情可查看下文 资金明细信息说明 */
    private String fund_bill_list;

    /** (String)优惠券信息。本交易支付时所使用的所有优惠券信息。详情可查看下表 优惠券信息说明 */
    private String vocher_detail_list;

    /** (String)回传参数，公共回传参数，如果请求时传递了该参数，则返回的异步通知会原样传回。本参数必须进行 UrlEncode 之后才可传入。 */
    private String passback_params;

    /** (Date)交易创建时间。格式为 yyyy-MM-dd HH:mm:ss */
    private String gmt_create;

    /** (Date)交易付款时间。格式为 yyyy-MM-dd HH:mm:ss */
    private String gmt_payment;

    /** (Date)交易退款时间。格式为 yyyy-MM-dd HH:mm:ss.S */
    private String gmt_refund;

    /** (Date)交易结束时间。格式为 yyyy-MM-dd HH:mm:ss */
    private String gmt_close;

    /** (Date)支付时间 格式为 yyyy-MM-dd HH:mm:ss */
    private String timestamp;

    /** (Date)通知的发送时间。格式为 yyyy-MM-dd HH:mm:ss */
    private String notify_time;

}
