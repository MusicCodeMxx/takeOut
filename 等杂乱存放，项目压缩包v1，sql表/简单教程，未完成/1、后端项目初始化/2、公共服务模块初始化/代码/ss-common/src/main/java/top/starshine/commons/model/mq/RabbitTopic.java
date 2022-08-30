package top.starshine.commons.model.mq;

/**
 * <h3>消息队列： 交换机,路由键,队列</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/26  下午 8:38  周二
 * @Description: hello world
 */
public abstract class RabbitTopic {

    // =============================== 登录微服务

    /**默认交换机*/
    public static final String EXCHANGE_DEFAULT = "ss.default.exchange";
    /**短信交换机*/
    public static final String EXCHANGE_SHORT_MESSAGE = "ss.short.message.exchange";
    /**短信路由键*/
    public static final String ROUTING_SHORT_MESSAGE = "ss.short.message.routing";
    /**验证码队列*/
    public static final String QUEUE_SHORT_MESSAGE = "ss.short.message.send.verification.code";

    // ================================ 订单微服务

    /** 订单业务的交换机 */
    public static final String EXCHANGE_ORDER = "ss.order.topic.exchange";

    /** 生产者投递消息的路由键 */
    public static final String ROUTING_CANCEL_AN_ORDER_ORDER = "ss.order.topic.routing.key";
    /* 消费订单取消的消息 */
    public static final String QUEUE_CONSUMER_CANCEL_AN_ORDER = "ss.order.topic.consumer.queue";

    /** 信息心跳时间到期后, 转死信投递消息路由键 */
    public static final String ROUTING_DEAD_CANCEL_AN_ORDER = "ss.order.topic.dead.routing.key";
    /** 消息心跳时间到期之后转死信队列 */
    public static final String QUEUE_DEAD_CANCEL_AN_ORDER = "ss.order.topic.dead.queue";

    /** 设定 TTL，单位是ms(毫秒)，下面的单位是 30分钟 */
    public static final int CANCEL_AN_ORDER_DEAD_TTL = 1800000;

    // ========= 保存订单生成的数据 ============ //

    /**订单生成保存路由器键*/
    public static final String ROUTING_ORDER_GENERATE_SAVE = "ss.order.generate.save.topic.routing.key";
    /***订单生成保存队列(消费者监听队列)*/
    public static final String QUEUE_ORDER_GENERATE_SAVE = "ss.order.generate.save.topic.consumer.queue";

    // ======== 通知商家有人退款 ============= //

    // 通知卖家处理退款
    // Notice_seller_handle_Refund
    // notice.seller.handle.refund
    // NOTICE_SELLER_HANDLE_REFUND

    /** 生产者投递消息的路由键 , 通知卖家处理退款*/
    public static final String ROUTING_NOTICE_SELLER_HANDLE_REFUND = "ss.notice.seller.handle.refund.routing.key";
    /** 消费订单取消的消息 , 通知卖家处理退款*/
    public static final String QUEUE_NOTICE_SELLER_HANDLE_REFUND = "ss.notice.seller.handle.refund.topic.consumer.queue";

    // ================================ 优惠券微服务

    // 回滚优惠券使用
    // RollbackCouponUse
    // rollback.coupon.use
    // ROLLBACK_COUPON_USE

    /** 优惠券业务的交换机 */
    public static final String EXCHANGE_COUPON = "ss.coupon.topic.exchange";

    // ============== 回滚用户使用的优惠券 ============ //

    /** 生产者投递消息的路由键 , 回滚优惠券使用 */
    public static final String ROUTING_ROLLBACK_COUPON_USE = "ss.rollback.coupon.use.routing.key";
    /** 消费订单取消的消息 , 回滚优惠券使用 */
    public static final String QUEUE_ROLLBACK_COUPON_USE = "ss.rollback.coupon.use.topic.consumer.queue";



    // ================================ 支付微服务

    /** 支付业务的交换机 */
    public static final String EXCHANGE_PAYMENT = "ss.payment.topic.exchange";

    // ============ 退款处理 ============== //

    // 系统处理退款
    // SystemHandleRefund
    // system.handle.refund
    // SYSTEM_HANDLE_REFUND

    /** 生产者投递消息的路由键 , 系统处理退款 */
    public static final String ROUTING_SYSTEM_HANDLE_REFUND = "ss.system.handle.refund.routing.key";
    /** 消费订单取消的消息 , 系统处理退款 */
    public static final String QUEUE_SYSTEM_HANDLE_REFUND = "ss.system.handle.refund.topic.consumer.queue";


    // =============================== 产品微服务




    // =============================== 购物车微服务



    // =============================== ？


}
