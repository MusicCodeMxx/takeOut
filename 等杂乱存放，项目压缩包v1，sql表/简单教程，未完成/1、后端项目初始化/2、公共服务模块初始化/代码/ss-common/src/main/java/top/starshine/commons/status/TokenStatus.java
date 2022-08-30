package top.starshine.commons.status;

/**
 * 令牌状态枚举
 * <li>4系列令牌状态码</li>
 * @author: starshine
 * @version: 1.0
 * @since: 2022/7/5  下午 12:08  周二
 * @Description:
 */
public enum TokenStatus implements R, java.io.Serializable{

    /** 400001 - 令牌无效 */
    INVALID_TOKEN(40001,"令牌无效"),

    /** 400002 - 令牌过期 */
    TOKEN_EXPIRED(40002,"令牌过期"),

    /** 400003 - 令牌载荷为空 */
    TOKEN_PAYLOAD_IS_EMPTY(40003,"令牌载荷为空"),

    /**40004,"你已退出登录"*/
    YOU_ARE_LOGGED_OUT(40004,"你已退出登录"),

    ERROR(40000,"令牌处理错误");

    /** 状态码 */
    private Integer code;

    /** 消息 */
    private String message;

    TokenStatus(Integer code, String message){
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
