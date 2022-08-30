package top.starshine.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.starshine.commons.converter.PaymentConverter;
import top.starshine.commons.dubbo.OrderDubboService;
import top.starshine.commons.entity.order.OrderDetail;
import top.starshine.commons.entity.paymenty.AlipayNotifyVc;
import top.starshine.commons.entity.paymenty.PaymentRecord;
import top.starshine.commons.exception.BadRequestException;
import top.starshine.commons.exception.InternalServerErrorException;
import top.starshine.commons.model.redis.CachePrefix;
import top.starshine.commons.status.OrderStatus;
import top.starshine.config.AlipayTemplate;
import top.starshine.service.PaymentRecordService;
import top.starshine.service.PaymentService;

/**
 * <h3>支付宝支付实现类</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/2  下午 9:15  周二
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayPaymentServiceImpl implements PaymentService {

    private final RedisTemplate redisTemplate;
    private final AlipayTemplate alipayTemplate;
    private final PaymentConverter paymentConverter;
    private final PaymentRecordService paymentRecordService;

    @DubboReference(cluster = "failback", interfaceClass = OrderDubboService.class,
            interfaceName = "top.top.starshine.dubbo.OrderDubboService", version = "1.0.0")
    private OrderDubboService orderDubboService;

    @Override
    public String payment(Long orderId) {

        // 远程调用订单微服务， 获取订单支付信息
        OrderDetail orderDetail = orderDubboService.findOrderById(orderId);
        if (null == orderDetail) {
            // 检查缓存中是否有订单信息
            Long expire = redisTemplate.getExpire(CachePrefix.RABBIT_MQ_ORDER_SAVE + orderId);
            if (null == expire || expire == -2L) throw new BadRequestException(OrderStatus.ORDER_DOES_NOT_EXIST);
            throw new InternalServerErrorException(OrderStatus.ORDER_GENERATING);
        }

        // 组合信息
        return alipayTemplate.paymentMobile(
                orderDetail.getOutTradeNo(),
                String.valueOf(orderDetail.getBillPrice()),
                orderDetail.getSubject(),
                orderDetail.getBody());
    }

    @Override
    public boolean callbackNotify(Object callbackData) {
        // 检查回调对象
        if (!(callbackData instanceof AlipayNotifyVc)) return false;
        return notify(callbackData,"支付宝同步回调");
    }

    @Override
    public boolean asyncNotify(Object asyncData) {
        // 检查回调对象
        if (!(asyncData instanceof AlipayNotifyVc)) return false;
        return notify(asyncData, "支付宝异步回调");
    }

    /**
     * 通知处理
     * @param data 支付宝回调通知数据
     * @param type 回调通知类型，如：支付宝异步回调
     * @return 操作结果
     */
    private boolean notify(Object data, String type){
        AlipayNotifyVc alipayNotifyVc = (AlipayNotifyVc) data;

        // 检查订单订单号是否存在
        String outTradeNo = alipayNotifyVc.getOut_trade_no();
        if (!StringUtils.hasText(outTradeNo))return false;

        // 转型
        PaymentRecord paymentRecord = paymentConverter.alipayNotifyVcToPaymentRecord(alipayNotifyVc);

        // 存根操作, 有记录就更新, 无记录就插入
        if (checkOrderIsEmpty(alipayNotifyVc.getOut_trade_no())){
            paymentRecord.setIsDelete(0);
            // 进行保存操作
            paymentRecordService.save(paymentRecord);
            paymentRecord = null;
        }else{
            new LambdaUpdateChainWrapper<>(paymentRecordService.getBaseMapper())
                    .eq(PaymentRecord::getOutTradeNo, paymentRecord.getOutTradeNo())
                    .eq(PaymentRecord::getIsDelete, 0)
                    .update(paymentRecord);
        }

        // 检查订单是否存在
        OrderDetail orderDetail = orderDubboService.findOrderByOutTradeNo(outTradeNo);
        if (null == orderDetail) return false;

        // 检查订单状态
        switch (orderDetail.getStatus()){
            case 1:
                // 将订单改为已付款
                return orderDubboService.orderPaymentSuccessful(orderDetail.getId(), type);
            case 2:
                return true;
        }
        // 订单其他状态就不处理
        return true;
    }

    /**
     * 检查存根是否存在
     * @param outTradeNo 订单号
     * @return true 存在, false 不存在
     */
    private boolean checkOrderIsEmpty(String outTradeNo){
        // 检查是否有存在存根
        Integer count = new LambdaQueryChainWrapper<>(paymentRecordService.getBaseMapper())
                .eq(PaymentRecord::getOutTradeNo,outTradeNo)
                .eq(PaymentRecord::getIsDelete, 0)
                .select(PaymentRecord::getOutTradeNo)
                .count();
        return null == count || count == 0;
    }
}
