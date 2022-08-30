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
public enum RefundOperationRecordTypeEnum {

    /**退款申请*/
    createRefund,
    /**卖家处理*/
    sellerProcessing,
    /**2系统退款*/
    systemRefund,
    /**3完成*/
    done,
    /**4系统退款异常*/
    systemRefundError,
    /**5卖拒绝退款*/
    SellerRefusesRefund,
    /**6买家取消退款*/
    buyerCancels,


}
