package top.starshine.commons.dubbo;

import top.starshine.commons.entity.paymenty.PaymentRecord;

import java.util.List;
import java.util.Set;

/**
 * <h3>支付微服务 Dubbo RPC 远程调用</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/5  下午 1:19  周五
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
public interface PaymentDubboService {

    /**
     * 查询订单的交易号
     * @param outTradeNos 订单号查询出支付成功的交易号
     */
    List<PaymentRecord> findTradeNoByOutTradeNos(Set<String> outTradeNos);

    /**
     * 查询订单的交易号
     * @param outTradeNo 订单编号
     * @return 支付存根对象
     */
    PaymentRecord findTradeNoByOutTradeNo(String outTradeNo);

}
