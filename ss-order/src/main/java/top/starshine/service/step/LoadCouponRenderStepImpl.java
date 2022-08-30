package top.starshine.service.step;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.starshine.commons.dubbo.CouponDubboService;
import top.starshine.commons.entity.order.TradeOrderDto;
import top.starshine.commons.handle.ThreadLocalCache;
import top.starshine.commons.model.renderstep.RenderStep;
import top.starshine.commons.model.renderstep.RenderStepEnums;

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
public class LoadCouponRenderStepImpl implements RenderStep {

    private static final RenderStepEnums LOAD_COUPON = RenderStepEnums.LOAD_COUPON;

    @DubboReference(cluster = "failback", interfaceClass = CouponDubboService.class,
            interfaceName = "top.top.starshine.dubbo.CouponDubboService", version = "1.0.0")
    private CouponDubboService couponDubboService;

    /**
     * 检查是否相同渲染枚举
     * @return {@link RenderStepEnums} 渲染枚举
     */
    @Override
    public boolean step(RenderStepEnums rs) {
        return LOAD_COUPON.equals(rs);
    }

    @Async
    @Override
    public void render(TradeOrderDto tod) {
        ThreadLocalCache.put(tod.getUser());
        try {
            // 异步加载优惠券列表
            tod.setCoupons(couponDubboService.getUserCoupons());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ThreadLocalCache.remove();
        }
    }

}
