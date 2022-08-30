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
 * <h3>退款存根信息表 映射</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/5  下午 2:48  周五
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Data // set get tostring hash...
@AllArgsConstructor // 全参构造器
@NoArgsConstructor // 无参构造器
@Accessors(chain = true) // 构建模式
@TableName("ss_trade_refund_record") // 用户表
@EqualsAndHashCode(callSuper = true) // 继承需要操作父类校验之类
public class TradeRefundRecord  extends BaseEntity implements java.io.Serializable {

    /**
     (Price)退款金额。 需要退款的金额，该金额不能大于订单金额，单位为元，支持两位小数。
     注：如果正向交易使用了营销，该退款金额包含营销金额，支付宝会按业务规则分配营销和买家自有资金分别退多少，
     默认优先退买家的自有资金。如交易总金额100元，用户支付时使用了80元自有资金和20元无资金流的营销券，
     商家实际收款80元。如果首次请求退款60元，则60元全部从商家收款资金扣除退回给用户自有资产；如果再请求退款40元，
     则从商家收款资金扣除20元退回用户资产以及把20元的营销券退回给用户（券是否可再使用取决于券的规则配置）。
     */
    private Double refundAmount;

    /** 退款总金额。指该笔交易累计已经退款成功的金额。 */
    private Double refundFee;

    /** 本次商户实际退回金额。说明：如需获取该值，需在入参query_options中传入 refund_detail_item_list。 */
    private Double sendBackFee;

    /** (String)退款原因说明。商家自定义，将在会在商户和用户的pc退款账单详情中展示 */
    private String refundReason;

    /**
     (String)退款请求号。标识一次退款请求，需要保证在交易号下唯一，如需部分退款，则此参数必传。
     注：针对同一次退款请求，如果调用接口失败或异常了，重试时需要保证退款请求号不能变更，
     防止该笔交易重复退款。支付宝会保证同样的退款请求号多次请求只会退一次。
     */
    private String outRequestNo;

    /**
     (OpenApiRoyaltyDetailInfoPojo[])退分账明细信息。
     注：
     1.当面付且非直付通模式无需传入退分账明细，系统自动按退款金额与订单金额的比率，从收款方和分账收入方退款，不支持指定退款金额与退款方。
     2.直付通模式，电脑网站支付，手机 APP 支付，手机网站支付产品，须在退款请求中明确是否退分账，从哪个分账收入方退，
     退多少分账金额；如不明确，默认从收款方退款，收款方余额不足退款失败。不支持系统按比率退款。
     */
    //private String alipayTradeRefundResponse;

    /** 网关返回码 对照表: https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=105806&docType=1 */
    private String code;

    /** 网关返回码描述, 对照表: https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=105806&docType=1 */
    private String msg;

    /** (String)支付宝交易号，支付宝交易凭证号*/
    private String tradeNo;

    /** 商户订单号 */
    private String outTradeNo;

    /** 用户的登录id */
    private String buyerLogonId;

    /** 本次退款是否发生了资金变化 */
    private String fundChange;

    /** 退款使用的资金渠道。只有在签约中指定需要返回资金明细，或者入参的query_options中指定时才返回该字段信息。 */
    private String refundDetailItemList;

    /** 交易在支付时候的门店名称 */
    private String storeName;

    /** 买家在支付宝的用户id */
    private String buyerUserId;

    /**签名,详情可查看,异步返回结果的验签*/
    private String sign;

    /** 退款时间 */
    private Date gmtRefundPay;

}
