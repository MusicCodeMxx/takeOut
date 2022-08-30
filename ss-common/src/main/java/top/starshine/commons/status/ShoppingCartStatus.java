package top.starshine.commons.status;

/**
 * <h3>购物车状态枚举</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/29  下午 12:37  周五
 * @Description: hello world
 */
public enum ShoppingCartStatus implements R, java.io.Serializable{

    /**50001,"购物车为空"*/
    IS_EMPTY(50001,"购物车为空"),

    /**50002,"购物车产品失效了"*/
    PRODUCT_IS_INVALID(50002,"购物车产品失效了"),

    /**50000,"购车处理错误"*/
    ERROR(50000,"购车处理错误");

    /** 状态码 */
    private Integer code;

    /** 消息 */
    private String message;

    ShoppingCartStatus(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
