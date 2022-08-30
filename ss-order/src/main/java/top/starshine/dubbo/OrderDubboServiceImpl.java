package top.starshine.dubbo;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import top.starshine.commons.dubbo.OrderDubboService;
import top.starshine.commons.entity.order.OrderDetail;
import top.starshine.commons.entity.order.OrderOperationRecord;
import top.starshine.commons.enums.OrderOperationRecordTypeEnum;
import top.starshine.service.OrderDetailService;
import top.starshine.service.OrderOperationRecordService;

import java.util.Date;

/**
 * <h3>订单 RPC 远程调用实现类</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/2  下午 9:18  周二
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@DubboService(
        // 集群容错模式
        cluster = "failback",
        // 服务降级
        mock = "return null",
        // 接口类型
        interfaceClass = OrderDubboService.class,
        // 接口名称
        interfaceName = "top.starshine.dubbo.OrderDubboService",
        // 接口版本
        version = "1.0.0"
)
@RequiredArgsConstructor
public class OrderDubboServiceImpl implements OrderDubboService {

    private final OrderDetailService orderDetailService;
    private final OrderOperationRecordService orderOperationRecordService;


    @Override
    public OrderDetail findOrderById(Long orderId) {
        return new LambdaQueryChainWrapper<>(orderDetailService.getBaseMapper())
                .eq(OrderDetail::getId,orderId)
                .eq(OrderDetail::getIsDelete,0)
                .one();
    }

    @Override
    public OrderDetail findOrderByOutTradeNo(String outTradeNo) {
        return new LambdaQueryChainWrapper<>(orderDetailService.getBaseMapper())
                .eq(OrderDetail::getOutTradeNo, outTradeNo)
                .eq(OrderDetail::getIsDelete,0)
                .one();
    }

    @Override
    public boolean orderPaymentSuccessful(Long orderId, String type) {
        // 先查询是否已支付
        OrderDetail orderDetail = new LambdaQueryChainWrapper<>(orderDetailService.getBaseMapper())
                .eq(OrderDetail::getId, orderId)
                .eq(OrderDetail::getIsDelete, 0)
                .select(OrderDetail::getStatus, OrderDetail::getId)
                .one();
        // 检查订单是否存在
        if (null == orderDetail) return false;

        // 订单已支付就返回 true
        if (orderDetail.getStatus().equals(2)) return true;

        if (orderDetail.getStatus().equals(1)){
            // 订单未支付状态进行修改为已支付
            boolean updateFlag = new LambdaUpdateChainWrapper<>(orderDetailService.getBaseMapper())
                    .set(OrderDetail::getStatus, 2)
                    .eq(OrderDetail::getId, orderId)
                    .eq(OrderDetail::getStatus, 1)
                    .eq(OrderDetail::getIsDelete, 0)
                    .update();

            // 保存操作记录
            if (updateFlag) orderOperationRecordService.save(
                    new OrderOperationRecord()
                            .setBeforeStatus(1)
                            .setAfterStatus(2)
                            .setCreateTime(new Date())
                            .setType(OrderOperationRecordTypeEnum.paymentSuccess.name())
                            .setOrderId(orderId)
                            .setNote( type + "通知, 用户支付成功，成功付款状态")
            );

            return updateFlag;
        }

        return false;
    }

}
