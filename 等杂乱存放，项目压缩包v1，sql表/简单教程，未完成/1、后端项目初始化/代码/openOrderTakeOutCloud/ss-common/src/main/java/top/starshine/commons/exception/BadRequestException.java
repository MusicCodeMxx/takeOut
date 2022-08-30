package top.starshine.commons.exception;

import top.starshine.commons.status.R;

/**
 * 自定义异常 - 继承运行异常 - 业务异常封装类 - 针对 400 HTTP 响应处理
 * <li>　400 (Bad Request/错误请求) </li>
 * <p>继承关系 BadRequestException => BussinessException => RuntimeException => ...</p>
 * @author: starshine
 * @version: 1.0
 * @since: 2022/7/5  下午 12:20  周二
 * @Description:
 */
public class BadRequestException extends BussinessException {

    /**
     * 自定义异常 - 继承运行异常 - 业务异常封装类 - 针对 400 HTTP 响应处理
     * <li>　400 (Bad Request/错误请求) </li>
     * <p>继承关系 BadRequestException => BussinessException => RuntimeException => ...</p>
     * @param r 基础响应状态封装枚举对象,统一枚举状态类
     */
    public BadRequestException(R r) {
        super(r);
    }

    /**
     * 自定义异常 - 继承运行异常 - 业务异常封装类 - 针对 400 HTTP 响应处理
     * <li>　400 (Bad Request/错误请求) </li>
     * <p>继承关系 BadRequestException => BussinessException => RuntimeException => ...</p>
     * @param code 业务状态码
     * @param message  消息体
     */
    public BadRequestException(int code, String message) {
        super(code, message);
    }

}
