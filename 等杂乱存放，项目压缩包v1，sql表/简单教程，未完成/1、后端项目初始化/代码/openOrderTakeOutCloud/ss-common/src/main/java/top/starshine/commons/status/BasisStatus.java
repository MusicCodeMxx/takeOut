package top.starshine.commons.status;


/**
 * 基础 HTTP 响应状态类
 * @author: starshine
 * @version: 1.0
 * @since: 2022/2/7  下午 7:44  周一
 * @Description: 状态封装
 */
public enum BasisStatus implements R, java.io.Serializable {

    /** 200 - 操作成功 */
    SUCCESS(200,"操作成功"),

    /** 400 - 请求错误 */
    BAO_REQUEST(400,"请求错误"),

    /** 400 - 请求参数不能为空 */
    REQUEST_PARAMETER_CANNOT_BE_EMPTY(400,"请求参数不能为空"),

    /** 401 - 您当前还未登录,无法操作 */
    UNAUTHORIZED(401,"您当前还未登录,无法操作"),

    /** 401 - 您当前还未登录,无法操作 */
    USER_DOES_NOT_EXIST(401,"用户不存在"),

    /** 500 - 全局结果统一处理出现了异常 */
    RUN_TIME_ERROR(500,"~服务姬运行中发生了错误"),

    /** 500 - 全局结果统一处理出现了异常 */
    NULL_POINTER_EXCEPTION(500,"空指针异常"),

    /** 500 - 全局结果统一处理出现了异常 */
    RESULT_ERROR(500,"全局结果统一处理出现了异常"),

    /** 9999 - 响应状态编码传入为空 */
    RESULT_VALUE_ERROR(999,"响应状态编码传入为空"),

    /**1003, "访问过于频繁，请稍后再试"*/
    LIMIT_ERROR(1003, "访问过于频繁，请稍后再试"),

    /** 9999 - 服务姬崩溃了 */
    ERROR(999,"服务姬崩溃了");

    /** 状态码 */
    private Integer code;

    /** 消息 */
    private String message;

    BasisStatus(Integer code, String message){
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
