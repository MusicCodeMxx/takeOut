package top.starshine.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import top.starshine.commons.converter.OrderConverter;
import top.starshine.commons.dubbo.CouponDubboService;
import top.starshine.commons.dubbo.PaymentDubboService;
import top.starshine.commons.dubbo.ShoppingCartDubboService;
import top.starshine.commons.entity.coupon.Coupon;
import top.starshine.commons.entity.order.*;
import top.starshine.commons.entity.paymenty.PaymentRecord;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.enums.OrderOperationRecordTypeEnum;
import top.starshine.commons.exception.BadRequestException;
import top.starshine.commons.exception.InternalServerErrorException;
import top.starshine.commons.handle.ThreadLocalCache;
import top.starshine.commons.model.redis.CachePrefix;
import top.starshine.commons.status.OrderStatus;
import top.starshine.commons.util.CollectionUtils;
import top.starshine.mapper.OrderDetailMapper;
import top.starshine.mq.producer.OrderSendMQ;
import top.starshine.service.ChildOrderDetailService;
import top.starshine.service.OrderDetailService;
import top.starshine.service.OrderOperationRecordService;

import java.util.*;

/**
 * <h3>订单服务接口实现类</h3>
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
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper,OrderDetail> implements OrderDetailService {

    private final OrderSendMQ orderSendMQ;
    private final RedisTemplate redisTemplate;
    private final OrderConverter orderConverter;
    private final ChildOrderDetailService childOrderDetailService;
    private final OrderOperationRecordService orderOperationRecordService;

    @DubboReference(cluster = "failback", interfaceClass = CouponDubboService.class,
            interfaceName = "top.top.starshine.dubbo.CouponDubboService", version = "1.0.0")
    private CouponDubboService couponDubboService;

    @DubboReference(cluster = "failback", interfaceClass = ShoppingCartDubboService.class,
            interfaceName = "top.top.starshine.dubbo.ShoppingCartDubboService", version = "1.0.0")
    private ShoppingCartDubboService shoppingCartDubboService;

    @DubboReference(cluster = "failback", interfaceClass = PaymentDubboService.class,
            interfaceName = "top.top.starshine.dubbo.PaymentDubboService", version = "1.0.0")
    private PaymentDubboService paymentDubboService;

    @Override
    @Transactional
    public boolean cancel(OrderDetail orderDetail) {
        // 修改订单状态
        boolean updateFlag = new LambdaUpdateChainWrapper<>(getBaseMapper())
                .set(OrderDetail::getStatus, 8)
                .eq(OrderDetail::getId, orderDetail.getId())
                .eq(OrderDetail::getUserId, orderDetail.getUserId())
                .eq(OrderDetail::getStatus, 1)
                .eq(OrderDetail::getIsDelete, 0)
                .update();
        // 保存操作记录
        if (updateFlag) orderOperationRecordService.save(
                new OrderOperationRecord()
                        .setBeforeStatus(1)
                        .setAfterStatus(8)
                        .setType(OrderOperationRecordTypeEnum.timeOutCancel.name())
                        .setCreateTime(new Date())
                        .setOrderId(orderDetail.getId())
                        .setNote("已达到订单过期时间，系统将订单关闭")
        );
        return updateFlag;
    }

    @Override
    @Transactional
    public void saveTradeBuildData(TradeOrderDto tod) {
        try{
            ThreadLocalCache.put(tod.getUser());// 存入本地线程缓存
            OrderDetail orderDetail = tod.getOrderDetail();

            // 先查询订单, 防止重试报已存在订单
            Integer count = new LambdaQueryChainWrapper<>(getBaseMapper())
                    .eq(OrderDetail::getOutTradeNo,orderDetail.getOutTradeNo())
                    .eq(OrderDetail::getId, orderDetail.getId())
                    .eq(OrderDetail::getIsDelete, 0)
                    .eq(OrderDetail::getStatus, 1)
                    .select(OrderDetail::getId)
                    .count();

            if (count == 0){
                // 保存主订单, 保存操作记录
                if (save(orderDetail)) orderOperationRecordService.save(
                        new OrderOperationRecord()
                                .setBeforeStatus(0)
                                .setAfterStatus(1)
                                .setType(OrderOperationRecordTypeEnum.awaitPayment.name())
                                .setCreateTime(new Date())
                                .setOrderId(orderDetail.getId())
                                .setNote("订单生成成功，等待付款")
                );
            }

            // 查询是否存在该订单下的子订单
            Integer childCount = new LambdaQueryChainWrapper<>(childOrderDetailService.getBaseMapper())
                                        .eq(ChildOrderDetail::getOutTradeNo, orderDetail.getOutTradeNo())
                                        .eq(ChildOrderDetail::getIsDelete, 0)
                                        .select(ChildOrderDetail::getId)
                                        .count();

            // 保存子订单
            if (childCount == 0) childOrderDetailService.saveBatch(tod.getChildOrderDetails());

            // 保存核销的优惠券
            Coupon coupon = tod.getCoupon();
            if (null != coupon) couponDubboService.updateUseCoupon(coupon);

            // 删除购物车缓存
            shoppingCartDubboService.clear();

            // 订单若未在固定时间内支付就取消的消息
            orderSendMQ.sendAutoTLLCancelAnOrder(orderDetail);

            // 删除 redis中的缓存
            redisTemplate.delete(CachePrefix.RABBIT_MQ_ORDER_SAVE + orderDetail.getId());

        } finally {
            ThreadLocalCache.remove();// 移除本地线程缓存
        }
    }

    @Override
    public IPage<OrderDetailVo> pageList(long current, long size) {
        // 获取当前登录的用户信息
        Long userId = ((User)ThreadLocalCache.getNotNull()).getId();

        // 获取当前用户的主订单
        Page<OrderDetail> orderDetailPage = new LambdaQueryChainWrapper<>(getBaseMapper())
                .eq(OrderDetail::getUserId, userId)
                .eq(OrderDetail::getIsDelete, 0)
                .orderByDesc(OrderDetail::getCreateTime)
                .select(OrderDetail::getId,
                        OrderDetail::getOriginalPrice,
                        OrderDetail::getOutTradeNo,
                        OrderDetail::getCouponPrice,
                        OrderDetail::getProductTotalNumber,
                        OrderDetail::getFreightPrice,
                        OrderDetail::getBillPrice,
                        OrderDetail::getStatus,
                        OrderDetail::getConsigneeName,
                        OrderDetail::getPhoneNumber,
                        OrderDetail::getCreateTime,
                        OrderDetail::getDetail,
                        OrderDetail::getSex,
                        OrderDetail::getLabel,
                        OrderDetail::getRemark)
                .page(new Page<>(current, size));
        // 检查是否获取到订单
        List<OrderDetail> orderDetails = orderDetailPage.getRecords();
        if (CollectionUtils.isEmpty(orderDetails)) return null;

        // 分拣出主键进行批量查询子订单
        Set<Long> orderIds = new HashSet<>(orderDetails.size());
        // 分拣出已支付的订单主键
        Set<String> outTradeNos = new HashSet<>(orderDetails.size());
        for (OrderDetail od : orderDetails) {
            orderIds.add(od.getId());
            // 2付款成功, 3制作, 4派送, 5派送完成, 6订单完成,9退款申请, 10退款成功
            switch (od.getStatus()){
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 9:
                case 10:
                    outTradeNos.add(od.getOutTradeNo());
                    break;
            }
        }

        // 远程调用查询该订单主键的支付宝交易编号
        List<PaymentRecord> tradeNoByOutTradeNos = paymentDubboService.findTradeNoByOutTradeNos(outTradeNos);

        //  批量查询出主订单绑定的子订单
        List<ChildOrderDetail> childOrderDetails = new LambdaQueryChainWrapper<>(childOrderDetailService.getBaseMapper())
                .in(ChildOrderDetail::getOrderId, orderIds)
                //.eq(ChildOrderDetail::getUserId, userId)
                .eq(ChildOrderDetail::getIsDelete, 0)
                .select(ChildOrderDetail::getOrderId,
                        ChildOrderDetail::getName,
                        ChildOrderDetail::getValue,
                        ChildOrderDetail::getImageDefUrl,
                        ChildOrderDetail::getPrice,
                        ChildOrderDetail::getAmount)
                .list();
        if (CollectionUtils.isEmpty(childOrderDetails)) throw new RuntimeException("订单不存在");

        // 批量查询主订单操作的时间
        List<OrderOperationRecord> orderOperationRecords = new LambdaQueryChainWrapper<>(orderOperationRecordService.getBaseMapper())
                .in(OrderOperationRecord::getOrderId,orderIds)
                .select(OrderOperationRecord::getOrderId,
                        OrderOperationRecord::getCreateTime,
                        OrderOperationRecord::getType)
                .list();


        // 将结果组合归队
        return orderDetailPage.convert(od -> {
            // 转视图对象
            OrderDetailVo orderDetailVo = orderConverter.orderDetailToOrderDetailVo(od);
            // 找出子订单
            List<ChildOrderDetailVo> childOrderDetailVos = new ArrayList<>();
            for (ChildOrderDetail cod : childOrderDetails) {
                if (od.getId().equals(cod.getOrderId())){
                    childOrderDetailVos.add(orderConverter.childOrderDetailToChildOrderDetailVo(cod));
                }
            }
            // 回填
            orderDetailVo.setChildOrderDetailVos(childOrderDetailVos);
            // 找出操作记录
            Map<String, Date> operationTime = new HashMap<>(10);
            // 订单创建时间
            operationTime.put("createTime",od.getCreateTime());
            if (CollectionUtils.isNotEmpty(orderOperationRecords)) {
                // 循环操作记录
                for (OrderOperationRecord oor : orderOperationRecords) {
                    // 判断是否是当前订单
                    if (od.getId().equals(oor.getOrderId())) {
                        String type = oor.getType();
                        // 检查类型是否空
                        if (StringUtils.hasText(type)){
                            // 将类型做字段, 创建时间作为值
                            operationTime.put(type, oor.getCreateTime());
                        }
                    }
                }
            }
            // 回填
            orderDetailVo.setOperationTime(operationTime);
            od = null;
            // 回填交易号
            if (CollectionUtils.isNotEmpty(tradeNoByOutTradeNos)){
                for (PaymentRecord pr : tradeNoByOutTradeNos) {
                    if (null == orderDetailVo.getOutTradeNo()) continue;
                    if (null == pr.getOutTradeNo()) continue;
                    if (null == pr.getTradeNo()) continue;
                    if (pr.getOutTradeNo().equals(orderDetailVo.getOutTradeNo())){
                        orderDetailVo.setTradeNo(pr.getTradeNo());
                    }
                }
            }
            return orderDetailVo;
        });
    }

    @Override
    public void cancelById(String id) {
        User user = ThreadLocalCache.getNotNull();

        // 查询订单
        OrderDetail orderDetail = new LambdaQueryChainWrapper<>(getBaseMapper())
                .eq(OrderDetail::getUserId, user.getId())
                .eq(OrderDetail::getId, id)
                .select(OrderDetail::getStatus)
                .eq(OrderDetail::getIsDelete, 0)
                .one();
        if (null == orderDetail) throw new BadRequestException(OrderStatus.ORDER_DOES_NOT_EXIST);

        // 0创建订单, 1待付款, 2付款成功, 3制作, 4派送, 5派送完成, 6订单完成, 7已取消, 8超时取消, 9退款申请, 10退款成功
        // 检查操作状态
        // 符合的订单状态: 0创建订单, 1待付款, 9退款申请
        switch (orderDetail.getStatus()){
            case 2:
            case 3:
            case 4:
            case 5: throw new BadRequestException(OrderStatus.OPERATION_ORDER_STATUS_ERROR);
            case 7:
            case 8: throw new BadRequestException(OrderStatus.OPERATION_ORDER_STATUS_ERROR);
            case 10: throw new BadRequestException(OrderStatus.OPERATION_ORDER_STATUS_ERROR);
        }

        try {

            // 必须是未支付才能取消订单
            if (new LambdaUpdateChainWrapper<>(getBaseMapper())
                    .set(OrderDetail::getStatus, 7)
                    .eq(OrderDetail::getId, id)
                    .update()){
                // 取消成功就记录
                orderOperationRecordService.save(
                        new OrderOperationRecord()
                                .setBeforeStatus(orderDetail.getStatus())
                                .setAfterStatus(7)
                                .setType(OrderOperationRecordTypeEnum.cancel.name())
                                .setCreateTime(new Date())
                                .setOrderId(Long.valueOf(id))
                                .setNote("用户主动取消订单"));
            }else {
                throw new BadRequestException(OrderStatus.OPERATION_ERROR);
            }
        } catch (NumberFormatException e) {
            throw new InternalServerErrorException(OrderStatus.ERROR);
        }
    }

    @Override
    public void deleteById(String id) {
        User user = ThreadLocalCache.getNotNull();
        // 符合状态(已完成, 取消, 超时取消)才能操作删除

        // 查询订单
        OrderDetail orderDetail = new LambdaQueryChainWrapper<>(getBaseMapper())
                .eq(OrderDetail::getUserId, user.getId())
                .eq(OrderDetail::getId, id)
                .select(OrderDetail::getStatus)
                .eq(OrderDetail::getIsDelete, 0)
                .one();
        if (null == orderDetail) throw new BadRequestException(OrderStatus.ORDER_DOES_NOT_EXIST);

        // 0创建订单, 1待付款, 2付款成功, 3制作, 4派送, 5派送完成, 6订单完成, 7已取消, 8超时取消, 9退款申请, 10退款成功
        // 检查操作状态
        // 符合的订单状态: 6订单完成, 7已取消, 8超时取消, 10退款成功
        switch (orderDetail.getStatus()){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5: throw new BadRequestException(OrderStatus.OPERATION_ORDER_STATUS_ERROR);
            case 9: throw new BadRequestException(OrderStatus.OPERATION_ORDER_STATUS_ERROR);
        }

        try {
            // 必须是未支付才能取消订单
            // 符合状态(已完成, 取消, 超时取消)才能操作删除
            if (new LambdaUpdateChainWrapper<>(getBaseMapper())
                    .set(OrderDetail::getIsDelete, 1)
                    .eq(OrderDetail::getId, id)
                    .update()){
                // 取消成功就记录
                orderOperationRecordService.save(
                        new OrderOperationRecord()
                                .setBeforeStatus(orderDetail.getStatus())
                                .setAfterStatus(-1)
                                .setType(OrderOperationRecordTypeEnum.delete.name())
                                .setCreateTime(new Date())
                                .setOrderId(Long.valueOf(id))
                                .setNote("用户主动删除订单"));
            }else {
                throw new BadRequestException(OrderStatus.OPERATION_ERROR);
            }
        } catch (NumberFormatException e) {
            throw new InternalServerErrorException(OrderStatus.ERROR);
        }
    }

}
