package top.starshine.commons.status;


/**
 * <h3>优惠券状态类 6</h3>
 * @author: starshine
 * @version: 1.0
 * @since: 2022/2/7  下午 7:44  周一
 * @Description: 状态封装
 */
public enum CouponStatus implements R, java.io.Serializable {

    /**60001-优惠券不存在*/
    COUPON_DOES_NOT_EXIST(60001,"优惠券不存在"),

    /**60002,"优惠券还没有开放领取"*/
    COUPON_RECEIVE_NO_START(60002,"优惠券还没有开放领取"),

    /**60003,"没有优惠券可以领取了"*/
    NO_COUPON(60003,"没有优惠券可以领取了"),

    /**60004,"该优惠券异常,无法领取"*/
    COUPON_ERROR(60004,"该优惠券异常,无法领取"),

    /**60005,"该优惠券已被冻结"*/
    FREEZE_COUPON(60005,"该优惠券已被冻结"),

    /**60006,"领取优惠券失败"*/
    RECEIVE_COUPON_ERROR(60006,"领取优惠券失败"),

    /**60007,"您已领取过了"*/
    YOU_RECEIVE_COUPON(60007,"您已领取过了"),

    /**60008,"每笔交易只能使用一张指定类别的满减券"*/
    SELECT_CATEGORY_COUPON(60008,"每笔交易只能使用一张指定类别的满减券"),

    /**60009,"该优惠券已过期"*/
    EXPIRED(60009,"该优惠券已过期"),

    /**60010,"消费金额未能达到该优惠券消费门槛"*/
    THRESHOLD_NOT_REACHED(60010,"消费金额未能达到该优惠券消费门槛"),

    THE_SELECTED_COUPON_PRICE_DOES_NOT_MATCH(60011,"所选优惠券面额不匹配"),

    /** 6999 - 服务姬崩溃了 */
    ERROR(69999,"优惠券服务崩溃了");

    /** 状态码 */
    private Integer code;

    /** 消息 */
    private String message;

    CouponStatus(Integer code, String message){
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
