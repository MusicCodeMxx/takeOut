package top.starshine.commons.status;

/**
 * 返回状态类的接口 - 统一规范状态类
 * @author: starshine
 * @version: 1.0
 * @since: 2022/3/7  下午 4:48  周一
 * @Description:
 */
public interface R {

    /** 获取状态码 */
     Integer getCode();

     /** 获取消息 */
     String getMessage();
}
