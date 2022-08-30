package top.starshine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.starshine.commons.entity.paymenty.PaymentRecord;
import top.starshine.commons.entity.paymenty.TradeRefundRecord;

import java.util.Map;

/**
 * <h3>退款存根服务接口</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/3  下午 7:03  周三
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
public interface TradeRefundRecordService extends IService<TradeRefundRecord> {

    /**
     * 保存系统退款存根
     * @param body 支付宝 响应体的数据
     * @param params 响应地址上的数据
     */
    void saveSystemRefund(String body, Map<String, String> params);

}
