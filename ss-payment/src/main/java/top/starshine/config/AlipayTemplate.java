package top.starshine.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付宝支付配置类
 * @author: starshine
 * @version: 1.0
 * @since: 2022/7/1  下午 3:10  周五
 * @Description:
 */
@Slf4j
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "starshine.alipay")
public class AlipayTemplate {

    /** 在支付宝创建的应用的ID */
    private String appId = "";

    /** 绑定的商家账号（PID） */
    private String sysServiceProviderId = "";

    /** 接口加密密钥 */
    private String encryptKey = "";

    /** 加密类型 */
    private String encryptType = "AES";

    /** 应用私钥, 商户私钥, 您的 PKCS8 格式 RSA2 私钥 */
    private String merchantPrivateKey = "";

    /** 应用公钥, 支付宝公钥, 查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。 */
    private String alipayPublicKey = "";

    /**
     * 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     * 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
     */
    private String notifyUrl = "http://公网IP:官网端口/api/ss-payment/aliPay/notify/async";

    /**
     * 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     * 同步通知，支付成功，一般跳转到成功页
     */
    private String returnUrl = "http://localhost:8898/api/ss-payment/aliPay/notify/callback";

    /** 签名方式 */
    private String signType = "RSA2";

    /** 字符编码格式 */
    private String charset = "UTF-8";

    /** 订单超时时间，到达超时时间后自动关闭订单不能再继续支付 */
    private String timeout = "30m";

    /** 正式支付宝网关； https://openapi.alipay.com/gateway.do */
    private String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // Json 编码
    private static final String JSON_FINAL = "json";
    // 桌面端支付标识
    private static final String FAST_INSTANT_TRADE_PAY = "FAST_INSTANT_TRADE_PAY";
    // 手机端支付标识
    private static final String QUICK_WAP_WAY = "QUICK_WAP_WAY";


    /**
     * 根据支付宝的配置生成一个支付客户端
     * @return 支付对象
     */
    private AlipayClient generateClient(){
        return new DefaultAlipayClient(gatewayUrl, appId, merchantPrivateKey, JSON_FINAL, charset, alipayPublicKey, signType, encryptKey , encryptType);
    }

    /**
     * 生成付款网址-移动端支付
     * @param outTradeNo 订单号
     * @param totalAmount 付款金额
     * @param subject 订单名称
     * @param body 商品描述
     * @return 付款地址
     */
    public String paymentMobile(String outTradeNo,String totalAmount, String subject, String body){
        // 创建一个支付请求, 设置请求参数
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        alipayRequest.setReturnUrl(returnUrl);// 支付成功支付回调地址
        alipayRequest.setNotifyUrl(notifyUrl);// 支付成功之后消息订阅地址
        alipayRequest.setNeedEncrypt(true);// 开启 AES 加密
        try {
            alipayRequest.setBizContent(new ObjectMapper().writeValueAsString(new BizContent(outTradeNo, totalAmount, subject, body, timeout, QUICK_WAP_WAY, sysServiceProviderId)));
            return this.generateClient().pageExecute(alipayRequest).getBody();
        } catch (JsonProcessingException | AlipayApiException e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 生成付款网址-桌面版
     * @param outTradeNo 订单号
     * @param totalAmount 付款金额
     * @param subject 订单名称
     * @param body 商品描述
     * @return 付款地址
     */
    public String paymentDesktop(String outTradeNo,String totalAmount, String subject,String body){
        // 创建一个支付请求, 设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(returnUrl);// 支付成功支付回调地址
        alipayRequest.setNotifyUrl(notifyUrl);// 支付成功之后消息订阅地址
        alipayRequest.setNeedEncrypt(true);// 开启 AES 加密
        try {
            alipayRequest.setBizContent(new ObjectMapper().writeValueAsString(new BizContent(outTradeNo, totalAmount, subject, body, timeout, FAST_INSTANT_TRADE_PAY, sysServiceProviderId)));
            return this.generateClient().pageExecute(alipayRequest).getBody();
        } catch (JsonProcessingException | AlipayApiException e) {
            e.printStackTrace();
            return "error";
        }
    }


    /**
     * 退款操作
     * @param tradeNo 退款支付宝交易订单(交易成功之后返回的交易号
     * @param outTradeNo 退款商家系统中的订单号(原支付时的订单号
     * @param refundAmount 退款金额
     * @param outRequestNo 退款商家退款订单号
     * @param refundReason 退款原因
     * @return 请求对象
     * @throws AlipayApiException 支付宝异常
     */
    public AlipayTradeRefundResponse tradeRefund(String tradeNo, String outTradeNo, String refundAmount, String outRequestNo, String refundReason) throws AlipayApiException, JsonProcessingException {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        /*
            AlipayTradeRefundRequest alipayTradeCloseRequest =new AlipayTradeRefundRequest();//请求参数集合对象,除了公共参数之外,所有参数都可通过此对象传递
            AlipayTradeRefundModel alipayTradeRefundModel =new AlipayTradeRefundModel();//退款的订单号,传入生成支付订单时的订单号即可
            alipayTradeRefundModel.setOutTradeNo(outTradeNo);//退款的原因
            alipayTradeRefundModel.setRefundAmount(refundAmount);//退款金额
            alipayTradeRefundModel.setRefundReason(refundReason);
            alipayTradeCloseRequest.setBizModel(alipayTradeRefundModel);
            refundResponse.getFundChange().equals("Y")
         */
        request.setBizContent(new ObjectMapper().writeValueAsString(new RefundBizContent(tradeNo, outTradeNo,refundAmount,outRequestNo, refundReason)));
        return this.generateClient().execute(request);
    }

    /**
     * 退款操作-返回布尔版本
     * @param tradeNo 退款支付宝交易订单
     * @param outTradeNo 退款商家系统中的订单号(原支付时的订单号
     * @param refundAmount 退款金额
     * @param outRequestNo 退款商家订单
     * @param refundReason 退款原因
     * @return 返回布尔
     * @throws AlipayApiException 支付宝异常
     */
    public boolean isTradeRefund(String tradeNo, String outTradeNo, String refundAmount, String outRequestNo, String refundReason) throws AlipayApiException, JsonProcessingException {
        try {
            return this.tradeRefund(tradeNo,outTradeNo,refundAmount,outRequestNo,refundReason).isSuccess();
        } catch (Exception e) {
            log.info("退款中发生异常");
            return false;
        }
    }

    /**
     * 调用 SDK 验证签名
     * <li>验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商家自身业务处理，校验失败返回failure</li>
     * <li>验签失败则记录异常日志，并在response中返回failure</li>
     * @param paramsMap 将异步通知中收到的所有参数都存放到 map 中
     * @return 布尔
     */
    public boolean rsaCheckV1( Map<String, String> paramsMap){
        try {
            return  AlipaySignature.rsaCheckV1(paramsMap, alipayPublicKey, charset, signType);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RefundBizContent{
        /** (特殊可选)商户订单号。订单支付时传入的商户订单号，商家自定义且保证商家系统中唯一。与支付宝交易号 trade_no 不能同时为空*/
        private String out_trade_no;
        /** (特殊可选)支付宝交易号。和商户订单号 out_trade_no 不能同时为空。 */
        private String trade_no;
        /** (必选)退款金额。
         需要退款的金额，该金额不能大于订单金额，单位为元，支持两位小数。
         注：如果正向交易使用了营销，该退款金额包含营销金额，支付宝会按业务规则分配营销和买家自有资金分别退多少，
         默认优先退买家的自有资金。如交易总金额100元，用户支付时使用了80元自有资金和20元无资金流的营销券，
         商家实际收款80元。如果首次请求退款60元，则60元全部从商家收款资金扣除退回给用户自有资产；如果再请求退款40元，
         则从商家收款资金扣除20元退回用户资产以及把20元的营销券退回给用户（券是否可再使用取决于券的规则配置）。 */
        private String refund_amount;
        /** (可选)退款原因说明。商家自定义，将在会在商户和用户的pc退款账单详情中展示 */
        private String refund_reason;
        /** (可选)退款请求号。
         标识一次退款请求，需要保证在交易号下唯一，如需部分退款，则此参数必传。
         注：针对同一次退款请求，如果调用接口失败或异常了，重试时需要保证退款请求号不能变更，防止该笔交易重复退款。
         支付宝会保证同样的退款请求号多次请求只会退一次。 */
        private String out_request_no;
        /** 退分账明细信息。
         注： 1.当面付且非直付通模式无需传入退分账明细，系统自动按退款金额与订单金额的比率，从收款方和分账收入方退款，不支持指定退款金额与退款方。
         2.直付通模式，电脑网站支付，手机 APP 支付，手机网站支付产品，须在退款请求中明确是否退分账，从哪个分账收入方退，退多少分账金额；
         如不明确，默认从收款方退款，收款方余额不足退款失败。不支持系统按比率退款。 */
        private String refund_royalty_parameters;
        /** (可选)查询选项。
         商户通过上送该参数来定制同步需要额外返回的信息字段，数组格式。
         支持：refund_detail_item_list：退款使用的资金渠道；deposit_back_info：触发银行卡冲退信息通知 */
        private String query_options;
        /** 从全参构造器检查必填数据是否传入 */
        public RefundBizContent(String tradeNo, String outTradeNo, String refundAmount, String outRequestNo, String refundReason) {
            if (!(StringUtils.hasText(tradeNo))) throw new RuntimeException("支付宝支付的交易单号不能为空");
            if (!(StringUtils.hasText(outTradeNo))) throw new RuntimeException("商家订单号不能为空");
            if (!(StringUtils.hasText(refundAmount))) throw new RuntimeException("退款金额不能为空");
            if (new BigDecimal("0.01").compareTo(BigDecimal.ZERO) <= 0) throw new RuntimeException("退款金额不能为零");
            if (!(StringUtils.hasText(outRequestNo))) throw new RuntimeException("退款不能为空");
            if (!(StringUtils.hasText(refundReason))) throw new RuntimeException("退款原因不能为空");
            this.trade_no = tradeNo;
            this.out_trade_no = outTradeNo;
            this.refund_amount = refundAmount;
            this.out_request_no = outRequestNo;
            this.refund_reason = refundReason;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BizContent{
        /** 商户订单号。订单支付时传入的商户订单号，商家自定义且保证商家系统中唯一。与支付宝交易号 trade_no 不能同时为空。，必填 */
        private String out_trade_no;
        /** 付款金额，必填 */
        private String total_amount;
        /** 订单名称，必填 */
        private String subject;
        /** 商品描述，可空 */
        private String body;
        /** 订单超时时间 */
        private String timeout_express;
        /** 产品代码 */
        private String product_code;
        /** 商品明细信息，按需传入 */
        //private String goods_detail;
        /** 扩展信息, 按需传入 */
        //private String extend_params;
        private String sys_service_provider_id;
        /** 从全参构造器检查必填数据是否传入 */
        public BizContent(String outTradeNo, String totalAmount, String subject, String body, String timeoutExpress, String productCode,String sysServiceProviderId) {
            if (!(StringUtils.hasText(outTradeNo))) throw new RuntimeException("支付订单号不能为空");
            if (!(StringUtils.hasText(totalAmount))) throw new RuntimeException("支付订金额不能为空");
            if (new BigDecimal("0.01").compareTo(BigDecimal.ZERO) <= 0) throw new RuntimeException("支付订金额不能为零");
            if (!(StringUtils.hasText(subject))) throw new RuntimeException("订单标题不能为空");
            this.out_trade_no = outTradeNo;
            this.total_amount = totalAmount;
            this.subject = subject;
            this.body = body;
            this.timeout_express = timeoutExpress;
            this.product_code = productCode;
            this.sys_service_provider_id = sysServiceProviderId;
        }
    }

}

