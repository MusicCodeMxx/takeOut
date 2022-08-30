package top.starshine.commons.model.renderstep;

/**
 * <h3>价格渲染,步骤声明,绑定执行步骤</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 5:06  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
public final class RenderStepStatement {

    /**
     * <h3>购物车购物车渲染</h3>
     * <ul>
     *     <li>初始化</li>
     *     <li>加载优惠券</li>
     *     <li>加载购物车中的产品列表</li>
     *     <li>订单生成</li>
     *     <li>计算出所有产品小计价格</li>
     *     <li>计算运费价格</li>
     *     <li>满减优惠券计算</li>
     *     <li>计算最终账单价格合计</li>
     *     <li>保存交易详情信息</li>
     * </ul>
     * 参考枚举对象{@link RenderStepEnums}
     */
    public static RenderStepEnums[] cartRender = {
            RenderStepEnums.INITIALIZATION,
            RenderStepEnums.LOAD_COUPON,
            RenderStepEnums.LOAD_SHOPPING_CART,
            RenderStepEnums.GENERATION,
            RenderStepEnums.COMPUTED_CHILD_ORDER_BILL_PRICE,
            RenderStepEnums.COMPUTED_FREIGHT_PRICE,
            RenderStepEnums.FULL_DISCOUNT,
            RenderStepEnums.COMPUTED_BILL_PRICE,
            RenderStepEnums.SAVE_TRADE_DETAILS_INFO,
    };

    // 私有化构造器
    private RenderStepStatement(){}
}
