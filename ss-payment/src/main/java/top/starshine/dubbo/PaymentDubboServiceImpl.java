package top.starshine.dubbo;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.util.StringUtils;
import top.starshine.commons.dubbo.PaymentDubboService;
import top.starshine.commons.entity.paymenty.PaymentRecord;
import top.starshine.commons.util.CollectionUtils;
import top.starshine.service.PaymentRecordService;

import java.util.List;
import java.util.Set;

/**
 * <h3>支付微服务 Dubbo RPC 远程调用实现类</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/5  下午 1:20  周五
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@DubboService(
        // 集群容错模式
        cluster = "failback",
        // 服务降级
        mock = "return null",
        // 接口类型
        interfaceClass = PaymentDubboService.class,
        // 接口名称
        interfaceName = "top.starshine.dubbo.PaymentDubboService",
        // 接口版本
        version = "1.0.0"
)
@RequiredArgsConstructor
public class PaymentDubboServiceImpl implements PaymentDubboService {

    private final PaymentRecordService paymentRecordService;

    @Override
    public List<PaymentRecord> findTradeNoByOutTradeNos(Set<String> outTradeNos) {
        if (CollectionUtils.isEmpty(outTradeNos)) return null;
        return  new LambdaQueryChainWrapper<>(paymentRecordService.getBaseMapper())
                .in(PaymentRecord::getOutTradeNo, outTradeNos)
                .eq(PaymentRecord::getIsDelete, 0)
                .select(PaymentRecord::getOutTradeNo, PaymentRecord::getTradeNo)
                .list();
    }

    @Override
    public PaymentRecord findTradeNoByOutTradeNo(String outTradeNo) {
        if (!StringUtils.hasText(outTradeNo)) return null;
        return new LambdaQueryChainWrapper<>(paymentRecordService.getBaseMapper())
                .eq(PaymentRecord::getOutTradeNo, outTradeNo)
                .eq(PaymentRecord::getIsDelete, 0)
                .select( PaymentRecord::getOutTradeNo, PaymentRecord::getTradeNo)
                .one();
    }

}
