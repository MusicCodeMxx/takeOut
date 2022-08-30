package top.starshine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.starshine.commons.entity.order.RefundRecord;

/**
 * <h3>交易退款服务接口</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/2  下午 9:19  周二
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
public interface TradeRefundService extends IService<RefundRecord> {

    /**
     * 退款申请
     * @param tradeRefund 申请数据对象
     */
    void apply(RefundRecord tradeRefund);

    /**
     * 退款确认
     * @param tradeRefund 申请数据对象
     */
    void confirmRefund(RefundRecord tradeRefund);
}
