package top.starshine.commons.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import top.starshine.commons.entity.paymenty.AlipayNotifyVc;
import top.starshine.commons.entity.paymenty.PaymentRecord;

/**
 * <h3>支付服务 Bean 转换器</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 3:04  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Mapper(componentModel = "spring")
public interface PaymentConverter {

    @Mappings({
            @Mapping(source = "total_amount", target = "totalAmount"),
            @Mapping(source = "receipt_amount", target = "receiptAmount"),
            @Mapping(source = "invoice_amount", target = "invoiceAmount"),
            @Mapping(source = "buyer_pay_amount", target = "buyerPayAmount"),
            @Mapping(source = "point_amount", target = "pointAmount"),
            @Mapping(source = "refund_fee", target = "refundFee"),
            @Mapping(source = "charset", target = "charset"),
            @Mapping(source = "method", target = "method"),
            @Mapping(source = "sign", target = "sign"),
            @Mapping(source = "auth_app_id", target = "authAppId"),
            @Mapping(source = "version", target = "version"),
            @Mapping(source = "sign_type", target = "signType"),
            @Mapping(source = "trade_no", target = "tradeNo"),
            @Mapping(source = "app_id", target = "appId"),
            @Mapping(source = "out_trade_no", target = "outTradeNo"),
            @Mapping(source = "out_biz_no", target = "outBizNo"),
            @Mapping(source = "buyer_id", target = "buyerId"),
            @Mapping(source = "seller_id", target = "sellerId"),
            @Mapping(source = "trade_status", target = "tradeStatus"),
            @Mapping(source = "notify_type", target = "notifyType"),
            @Mapping(source = "notify_id", target = "notifyId"),
            @Mapping(source = "subject", target = "subject"),
            @Mapping(source = "body", target = "body"),
            @Mapping(source = "fund_bill_list", target = "fundBillList"),
            @Mapping(source = "vocher_detail_list", target = "vocherDetailList"),
            @Mapping(source = "passback_params", target = "passbackParams"),
            @Mapping(source = "gmt_create", target = "gmtCreate",dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(source = "gmt_payment", target = "gmtPayment",dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(source = "gmt_refund", target = "gmtRefund",dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(source = "gmt_close", target = "gmtClose",dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(source = "timestamp", target = "timestamp",dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(source = "notify_time", target = "notifyTime",dateFormat = "yyyy-MM-dd HH:mm:ss"),
    })
    PaymentRecord alipayNotifyVcToPaymentRecord(AlipayNotifyVc source);

}
