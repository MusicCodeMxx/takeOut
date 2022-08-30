package top.starshine.commons.enums;

/**
 * <h3>订单操作记录类型枚举</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/4  上午 12:33  周四
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
public enum OrderOperationRecordTypeEnum {

    /** 1创建订单*/
    delete,
    /**0创建订单*/
    create,
    /**1待付款*/
    awaitPayment,
    /**2付款成功*/
    paymentSuccess,
    /**3商家制作*/
    make,
    /**4配送员派送*/
    outOfDelivery,
    /**5派送完成*/
    deliveryCompleted,
    /**6订单完成*/
    orderCompleted,
    /**7已取消*/
    cancel,
    /**8超时未支付已自动取消*/
    timeOutCancel,
    /**9退款申请*/
    refund,
    /**10退款成功*/
    refundSuccess,

}
