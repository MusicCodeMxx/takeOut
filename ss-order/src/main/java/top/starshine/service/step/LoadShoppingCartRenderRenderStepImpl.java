package top.starshine.service.step;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.starshine.commons.dubbo.ShoppingCartDubboService;
import top.starshine.commons.entity.order.TradeOrderDto;
import top.starshine.commons.entity.shoppingcar.ShoppingCartVo;
import top.starshine.commons.exception.BadRequestException;
import top.starshine.commons.model.renderstep.RenderStep;
import top.starshine.commons.model.renderstep.RenderStepEnums;
import top.starshine.commons.status.ShoppingCartStatus;

import java.util.List;

/**
 * <h3>加载优惠券渲染步骤</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 5:11  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Slf4j
@Service
public class LoadShoppingCartRenderRenderStepImpl implements RenderStep {

    private static final RenderStepEnums LOAD_SHOPPING_CART = RenderStepEnums.LOAD_SHOPPING_CART;

    @DubboReference(cluster = "failback", interfaceClass = ShoppingCartDubboService.class,
            interfaceName = "top.top.starshine.dubbo.ShoppingCartDubboService", version = "1.0.0")
    private ShoppingCartDubboService shoppingCartDubboService;

    /**
     * 检查是否相同渲染枚举
     * @return {@link RenderStepEnums} 渲染枚举
     */
    @Override
    public boolean step(RenderStepEnums rs) {
        return LOAD_SHOPPING_CART.equals(rs);
    }

    @Override
    public void render(TradeOrderDto tod) {
        log.info("加载购物车数据");
        // 获取购物车信息
        List<ShoppingCartVo> list = shoppingCartDubboService.list();
        if (CollectionUtils.isEmpty(list)) throw new BadRequestException(ShoppingCartStatus.IS_EMPTY);
        tod.addChildOrderDetails(list);
    }

}
