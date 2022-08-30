package top.starshine.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.starshine.commons.dubbo.PaymentDubboService;
import top.starshine.commons.entity.order.*;
import top.starshine.commons.entity.paymenty.PaymentRecord;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.enums.OrderOperationRecordTypeEnum;
import top.starshine.commons.enums.RefundOperationRecordTypeEnum;
import top.starshine.commons.exception.BadRequestException;
import top.starshine.commons.exception.InternalServerErrorException;
import top.starshine.commons.handle.ThreadLocalCache;
import top.starshine.commons.status.OrderStatus;
import top.starshine.mapper.TradeRefundMapper;
import top.starshine.mq.producer.OrderSendMQ;
import top.starshine.service.OrderOperationRecordService;
import top.starshine.service.RefundOperationRecordService;
import top.starshine.service.TradeRefundService;

import java.util.Date;

/**
 * <h3>申请退款服务接口实现类</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/2  下午 9:19  周二
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeRefundServiceImpl extends ServiceImpl<TradeRefundMapper, RefundRecord> implements TradeRefundService {

    private final OrderSendMQ orderSendMQ;
    private final OrderDetailServiceImpl orderDetailService;
    private final OrderOperationRecordService orderOperationRecordService;
    private final RefundOperationRecordService refundOperationRecordService;

    @DubboReference(cluster = "failback", interfaceClass = PaymentDubboService.class,
            interfaceName = "top.top.starshine.dubbo.PaymentDubboService", version = "1.0.0")
    private PaymentDubboService paymentDubboService;

    @Override
    @Transactional
    public void apply(RefundRecord tradeRefund) {
        Long userId = ((User)ThreadLocalCache.getNotNull()).getId();

        // 检查状态
        OrderDetail orderDetail = new LambdaQueryChainWrapper<>(orderDetailService.getBaseMapper())
                .eq(OrderDetail::getId, tradeRefund.getOrderId())
                .eq(OrderDetail::getUserId, userId)
                .eq(OrderDetail::getIsDelete, 0)
                .select(OrderDetail::getStatus,
                        OrderDetail::getOutTradeNo,
                        OrderDetail::getBillPrice)
                .one();

        if (null == orderDetail) throw new BadRequestException(OrderStatus.ORDER_DOES_NOT_EXIST);

        /*
            什么状态可以退款?
                 2付款成功,
                 3制作,
                 4派送,
                 5派送完成,
         */
        switch (orderDetail.getStatus()){
            case 0:
            case 1:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                throw new BadRequestException(OrderStatus.OPERATION_ORDER_STATUS_ERROR);
        }

        // 检查是否提交过
        Integer count = new LambdaQueryChainWrapper<>(getBaseMapper())
                .eq(RefundRecord::getOrderId, tradeRefund.getOrderId())
                .eq(RefundRecord::getUserId, userId)
                .count();
        if (count != null && count > 0) throw new BadRequestException(OrderStatus.YOU_HAVE_APPLIED);

        // 将订单改为退款状态
        boolean update = new LambdaUpdateChainWrapper<>(orderDetailService.getBaseMapper())
                .set(OrderDetail::getStatus, 9)
                .eq(OrderDetail::getId, tradeRefund.getOrderId())
                .eq(OrderDetail::getUserId, userId)
                .eq(OrderDetail::getIsDelete, 0)
                .update();

        // 保存订单操作记录
        if (update)orderOperationRecordService.save(
                new OrderOperationRecord()
                        .setBeforeStatus(orderDetail.getStatus())
                        .setAfterStatus(9)
                        .setType(OrderOperationRecordTypeEnum.refund.name())
                        .setCreateTime(new Date())
                        .setOrderId(tradeRefund.getOrderId())
                        .setNote("买家发起退款")
        );

        // 创建退款对象, 买家创建 => 卖家处理
        tradeRefund.setUserId(userId)
                .setStatus(1)
                .setType(1)
                .setOutTradeNo(orderDetail.getOutTradeNo())
                .setPrice(orderDetail.getBillPrice());

        // 保存到数据库
        boolean save = save(tradeRefund);

        // 保存退款操作记录
        if (save){
            refundOperationRecordService.save(
                    new RefundOperationRecord()
                            .setBeforeStatus(0)
                            .setAfterStatus(1)
                            .setNote("买家发起退款，申请原因：" + tradeRefund.getBuyerReason())
                            .setType(RefundOperationRecordTypeEnum.createRefund.name())
                            .setRefundId(tradeRefund.getId())
            );
            // MQ 通知商家处理
            orderSendMQ.sendNoticeSellerHandleRefund(tradeRefund);
        }else{
            // 保存失败就抛异常
            throw new InternalServerErrorException(OrderStatus.TRADE_REFUND_ERROR);
        }
    }

    @Override
    @Transactional // 都在同一个服务内容，可以全部回滚
    public void confirmRefund(RefundRecord tradeRefund) {

        // 查询订单的状态
        OrderDetail orderDetail = new LambdaQueryChainWrapper<>(orderDetailService.getBaseMapper())
                .eq(OrderDetail::getId, tradeRefund.getOrderId())
                .eq(OrderDetail::getUserId, tradeRefund.getUserId())
                .eq(OrderDetail::getIsDelete, 0)
                .select(OrderDetail::getStatus, OrderDetail::getBillPrice)
                .one();

        log.warn("查询状态=>{}",orderDetail);

        if (null == orderDetail) throw new BadRequestException(OrderStatus.TRADE_REFUND_ERROR);

        // 查询申请是否存在
        //RefundRecord one = new LambdaQueryChainWrapper<>(getBaseMapper())
        //        .eq(RefundRecord::getId, tradeRefund.getId())
        //        .eq(RefundRecord::getIsDelete, 0)
        //        .one();
        /*
            检查状态，这里要重新检查一次，防止用户有突然不退款了，因还没有搭建 admin 后端，所以段代码不写了
         */

        // 处理申请，卖家处理 => 系统退款, 更新状态
        RefundRecord refundRecord = new RefundRecord().setStatus(2).setHandleReply("跳过步骤");
        tradeRefund.setStatus(2).setHandleReply("跳过步骤");// 要赋值到退款对象
        refundRecord.setId(tradeRefund.getId());


        // 保存处理申请
        boolean save = updateById(refundRecord);

        // 保存操作记录
        if (save){
            refundOperationRecordService.save(
                    new RefundOperationRecord()
                            .setBeforeStatus(1)
                            .setAfterStatus(2)
                            .setNote("卖家操作退款处理，卖家答复："+ tradeRefund.getHandleReply())
                            .setType(RefundOperationRecordTypeEnum.sellerProcessing.name())
                            .setRefundId(tradeRefund.getId())
            );
        }
        //else{
            // 保存失败通知人工台
        //}


        /*
            什么状态可以完成退款?
                9退款申请,
         */
        // if (null == orderDetail ) // 通知人工台

        // 修改订单的状态改为已退款成功
        // 将订单改为退款状态
        boolean update = new LambdaUpdateChainWrapper<>(orderDetailService.getBaseMapper())
                .set(OrderDetail::getStatus, 10)
                .eq(OrderDetail::getId, tradeRefund.getOrderId())
                .eq(OrderDetail::getUserId, tradeRefund.getUserId())
                .eq(OrderDetail::getIsDelete, 0)
                .update();

        // 保存订单操作记录
        if (update)orderOperationRecordService.save(
                new OrderOperationRecord()
                        .setBeforeStatus(9)
                        .setAfterStatus(10)
                        .setType(OrderOperationRecordTypeEnum.refundSuccess.name())
                        .setCreateTime(new Date())
                        .setOrderId(tradeRefund.getOrderId())
                        .setNote("卖家同意退款")
        );

        PaymentRecord tradeNoByOutTradeNo = paymentDubboService.findTradeNoByOutTradeNo(tradeRefund.getOutTradeNo());
        if (null == tradeNoByOutTradeNo) {
            log.warn("订单号{}，支付存根不存在",tradeRefund.getOutTradeNo());
            return;
        }

        // MQ 支付微服务处理退款
        orderSendMQ.sendSystemHandleRefund(
                new TradeRefundDto(tradeRefund)
                        .setTradeNo(tradeNoByOutTradeNo.getTradeNo())
                        .setRefundReason("买家申请退款，卖家同意退款")
        );

    }

}
