package top.starshine.service;

/**
 * <h3>支付服务接口</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/2  下午 9:12  周二
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
public interface PaymentService {

    /**
     * 对已生成的订单进行支付
     * @param orderId 订单号
     * @return 数据
     */
    String payment(Long orderId);

    /**
     * 同步回调接口
     * @param callbackData 回调的数据
     * @return 操作结果
     */
    boolean callbackNotify(Object callbackData);

    /**
     * 异步回调接口
     * @param asyncData 异步回调的数据
     * @return 操作结果
     */
    boolean asyncNotify(Object asyncData);
}
