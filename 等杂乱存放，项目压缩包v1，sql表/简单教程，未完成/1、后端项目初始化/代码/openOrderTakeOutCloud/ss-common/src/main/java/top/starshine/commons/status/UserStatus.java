package top.starshine.commons.status;

/**
 * 用户业务状态枚举类
 * @author: starshine
 * @version: 1.0
 * @since: 2022/7/5  下午 12:51  周二
 * @Description:
 */
public enum UserStatus implements R, java.io.Serializable{

    /**30001,"获取本地缓存为空"*/
    GET_LOCAL_CACHE_IS_EMPTY(30001,"获取本地缓存为空"),

    /**30000,"用户内部处理错误"*/
    ERROR(30000,"用户内部处理错误");

    /** 状态码 */
    private Integer code;

    /** 消息 */
    private String message;

    UserStatus(Integer code, String message){
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
