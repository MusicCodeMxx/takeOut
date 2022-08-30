package top.starshine.commons.exception;

import top.starshine.commons.status.R;

/**
 * 自定义异常 - 继承运行异常 - 业务异常封装类 - 针对 200 HTTP 响应处理
 * <p>继承关系 SuccessException => BussinessException => RuntimeException => ...</p>
 * @author starshine
 * @version 1.0
 * @Description starsh.top
 * @since 2022/2/4  下午 11:20  周五
 * @description:
 */
public class SuccessException  extends BussinessException  {

    /**
     * 自定义异常 - 继承运行异常 - 业务异常封装类 - 针对 200 HTTP 响应处理
     * <p>继承关系 SuccessException => BussinessException => RuntimeException => ...</p>
     * @param r 基础响应状态封装枚举对象,统一枚举状态类
     */
    public SuccessException(R r) {
        super(r);
    }

    /**
     * 自定义异常 - 继承运行异常 - 业务异常封装类 - 针对 200 HTTP 响应处理
     * <p>继承关系 SuccessException => BussinessException => RuntimeException => ...</p>
     * @param code 业务状态码
     * @param message 消息体
     */
    public SuccessException(int code, String message) {
        super(code, message);
    }
}
