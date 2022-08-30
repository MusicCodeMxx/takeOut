package top.starshine.commons.model.renderstep;

/**
 * <h3>渲染枚举</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 5:06  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
public enum RenderStepEnums {

    /**初始化*/
    INITIALIZATION("初始化"),
    /**加载优惠券*/
    LOAD_COUPON("加载优惠券"),
    /**加载购物车中的产品列表*/
    LOAD_SHOPPING_CART("加载购物车中的产品列表"),
    /**初始化订单*/
    GENERATION("订单生成"),
    /**计算出所有产品小计价格*/
    COMPUTED_CHILD_ORDER_BILL_PRICE("计算出所有产品小计价格"),
    /**计算运费价格*/
    COMPUTED_FREIGHT_PRICE("计算运费价格"),
    /**全场和指定分类满减优惠券计算*/
    FULL_DISCOUNT("满减优惠券计算"),
    /**计算最终账单价格合计*/
    COMPUTED_BILL_PRICE("计算最终账单价格合计"),
    /**保存交易详情信息*/
    SAVE_TRADE_DETAILS_INFO("保存交易详情信息");

    /** 描述 */
    private String distribution;

    /** 获取描述 */
    public String getDistribution() {
        return distribution;
    }

    /**购物车渲染枚举*/
    RenderStepEnums(String distribution) {
        this.distribution = distribution;
    }

}
