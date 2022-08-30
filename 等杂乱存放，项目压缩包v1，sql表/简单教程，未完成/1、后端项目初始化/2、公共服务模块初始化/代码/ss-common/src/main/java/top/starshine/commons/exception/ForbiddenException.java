package top.starshine.commons.exception;

import top.starshine.commons.status.R;

/**
 * 自定义异常 - 继承运行异常 - 业务异常封装类 - 针对 403 HTTP 响应处理
 * <li>　403 (Forbidden/禁止) </li>
 * <p>继承关系 ForbiddenException => BussinessException => RuntimeException => ...</p>
 * @author: starshine
 * @version: 1.0
 * @since: 2022/7/5  下午 12:33  周二
 * @Description:
 */
public class ForbiddenException extends BussinessException {

    /**
     * 自定义异常 - 继承运行异常 - 业务异常封装类 - 针对 403 HTTP 响应处理
     * <li>　403 (Forbidden/禁止) </li>
     * <p>继承关系 ForbiddenException => BussinessException => RuntimeException => ...</p>
     * @param r 基础响应状态封装枚举对象,统一枚举状态类
     */
    public ForbiddenException(R r) {
        super(r);
    }

    /**
     * 自定义异常 - 继承运行异常 - 业务异常封装类 - 针对 403 HTTP 响应处理
     * <li>　403 (Forbidden/禁止) </li>
     * <p>继承关系 ForbiddenException => BussinessException => RuntimeException => ...</p>
     * @param code 业务状态码
     * @param message 息体
     */
    public ForbiddenException(int code, String message) {
        super(code, message);
    }

}
