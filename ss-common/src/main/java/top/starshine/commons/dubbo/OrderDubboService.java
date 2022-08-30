package top.starshine.commons.dubbo;

import top.starshine.commons.entity.order.OrderDetail;

/**
 * <h3>订单微服务 RPC 远程调用接口</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/2  下午 9:17  周二
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
public interface OrderDubboService {

    /**
     * 通过订单主键找到未删除的订单详细信息
     * @param orderId 订单主键
     * @return 订单详细信息
     */
    OrderDetail findOrderById(Long orderId);

    /**
     * 通过订单号查询到订单信息
     * @param outTradeNo 订单号
     * @return 订单信息对象
     */
    OrderDetail findOrderByOutTradeNo(String outTradeNo);

    /**
     * 支付微服务, 收到支付成功回调之后进行修改订单状态
     * @param orderId 订单主键
     * @param type 支付回调类型，如：支付宝同步调用
     * @return 操作结果
     */
    boolean orderPaymentSuccessful(Long orderId, String type);

}
