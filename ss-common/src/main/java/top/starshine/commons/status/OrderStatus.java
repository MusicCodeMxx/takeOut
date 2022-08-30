package top.starshine.commons.status;

/**
 * <h3>订单状态枚举</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/2  下午 9:36  周二
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
public enum OrderStatus implements R, java.io.Serializable{

    /**70001, "该订单不存在"*/
    ORDER_DOES_NOT_EXIST(70001, "该订单不存在"),

    /**70002, "运费不一致"*/
    FREIGHT_PRICE_DOES_NOT_MATCH(70002, "运费不一致"),

    /**70003,"订单总价不一致"*/
    ORIGINAL_PRICE_DOES_NOT_MATCH(70003,"订单总价不一致"),

    /**70004,"订单生成中，请重试"*/
    ORDER_GENERATING(70004,"订单生成中，请重试"),

    /**70005,"优惠券的减免面额大于原价"*/
    COUPON_PRICE_GT_ORIGINAL_PRICE(70005,"优惠券的减免面额大于原价"),

    /**70006,"订单编号为空"*/
    ORDER_ID_IS_EMPTY(70006,"订单编号为空"),

    /**70007,"操作错误"*/
    OPERATION_ERROR(70007,"操作错误"),

    /**70008,"操作订单状态错误"*/
    OPERATION_ORDER_STATUS_ERROR(70008,"操作订单状态错误"),

    /**70009,"您已申请过退款了"*/
    YOU_HAVE_APPLIED(70009,"您已申请过退款了"),

    /*70010,"申请退款异常，请重试"*/
    TRADE_REFUND_ERROR(70010,"申请退款异常，请重试"),

    /**70000,"订单处理错误"*/
    ERROR(70000,"订单处理错误");

    /** 状态码 */
    private Integer code;

    /** 消息 */
    private String message;

    OrderStatus(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    /** 获取状态码 */
    @Override
    public Integer getCode() {
        return code;
    }

    /** 获取消息 */
    @Override
    public String getMessage() {
        return message;
    }
}
