package top.starshine.commons.exception;

import top.starshine.commons.status.R;

/**
 * 自定义异常 - 继承运行异常 - 业务异常封装类 - 针对 500 HTTP 响应处理
 * <li>　500 (Internal Server Error/内部服务器错误) </li>
 * <p>继承关系 InternalServerErrorException => BussinessException => RuntimeException => ...</p>
 * @author: starshine
 * @version: 1.0
 * @since: 2022/7/5  下午 12:36  周二
 * @Description:
 */
public class InternalServerErrorException extends BussinessException{

    /**
     * 自定义异常 - 继承运行异常 - 业务异常封装类 - 针对 500 HTTP 响应处理
     * <li>　500 (Internal Server Error/内部服务器错误) </li>
     * <p>继承关系 InternalServerErrorException => BussinessException => RuntimeException => ...</p>
     * @param r 基础响应状态封装枚举对象,统一枚举状态类
     */
    public InternalServerErrorException(R r) {
        super(r);
    }

    /**
     * 自定义异常 - 继承运行异常 - 业务异常封装类 - 针对 500 HTTP 响应处理
     * <li>　500 (Internal Server Error/内部服务器错误) </li>
     * <p>继承关系 InternalServerErrorException => BussinessException => RuntimeException => ...</p>
     * @param code 业务状态码
     * @param message 消息体
     */
    public InternalServerErrorException(int code, String message) {
        super(code, message);
    }

}
