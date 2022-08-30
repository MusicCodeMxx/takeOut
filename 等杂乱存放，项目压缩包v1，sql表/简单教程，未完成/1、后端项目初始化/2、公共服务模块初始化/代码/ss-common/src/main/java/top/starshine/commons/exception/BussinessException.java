package top.starshine.commons.exception;


import top.starshine.commons.status.R;

/**
 * 自定义异常父类 - 继承运行异常 - 业务异常封装类基础类 - 不要动
 * <li>　500 (Internal Server Error/内部服务器错误) </li>
 * <p>继承关系 BussinessException => RuntimeException => ...</p>
 * @author starshine
 * @version 1.0
 * @Description starsh.top
 * @since 2022/2/4  下午 11:20  周五
 * @description:
 */
public class BussinessException extends RuntimeException  {

    /** 状态码 */
    private Integer code;

    /**
     * 自定义异常父类 - 继承运行异常 - 业务异常封装类基础类 - 不要动
     * <li>　500 (Internal Server Error/内部服务器错误) </li>
     * <p>继承关系 BussinessException => RuntimeException => ...</p>
     * @param r 基础响应状态封装枚举对象,统一枚举状态类
     */
    public BussinessException(R r){
        super(r.getMessage());
        this.code = r.getCode();
    }

    /**
     * 自定义异常父类 - 继承运行异常 - 业务异常封装类基础类 - 不要动
     * <li>　500 (Internal Server Error/内部服务器错误) </li>
     * <p>继承关系 BussinessException => RuntimeException => ...</p>
     * @param code 业务状态码
     * @param message 消息体
     */
    public BussinessException(int code, String message){
        super(message);
        this.code = code;
    }

    /** 获取状态码 */
    public Integer getStatus() {
        return code;
    }

}
