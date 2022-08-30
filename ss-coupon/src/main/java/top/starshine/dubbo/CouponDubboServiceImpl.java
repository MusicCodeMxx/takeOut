package top.starshine.dubbo;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import top.starshine.commons.converter.CouponConverter;
import top.starshine.commons.dubbo.CouponDubboService;
import top.starshine.commons.entity.coupon.Coupon;
import top.starshine.commons.entity.coupon.CouponVo;
import top.starshine.service.CouponService;

import java.util.List;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 2:42  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@DubboService(
        // 集群容错模式
        cluster = "failback",
        // 服务降级
        mock = "return null",
        // 接口类型
        interfaceClass = CouponDubboService.class,
        // 接口名称
        interfaceName = "top.top.starshine.dubbo.CouponDubboService",
        // 接口版本
        version = "1.0.0"
)
@RequiredArgsConstructor
public class CouponDubboServiceImpl implements CouponDubboService {

    private final CouponService couponService;
    private final CouponConverter couponConverter;

    @Override
    public List<CouponVo> getUserCouponList() {
        return couponConverter.couponsToCouponVos(couponService.meCouponList());
    }

    @Override
    public List<Coupon> getUserCoupons() {
        return couponService.meCouponList();
    }

    @Override
    public void updateUseCoupon(Coupon coupon) {

        // 检查是否被该订单核销了
        Integer count = new LambdaQueryChainWrapper<>(couponService.getBaseMapper())
                .eq(Coupon::getUseOutTradeNo, coupon.getUseOutTradeNo())
                .ne(Coupon::getStatus, 0)
                .select(Coupon::getId)
                .count();
        if (count > 0) return;

        new LambdaUpdateChainWrapper<>(couponService.getBaseMapper())
                .set(Coupon::getStatus,1)
                .set(Coupon::getUseTime,coupon.getUseTime())
                .set(Coupon::getUseOutTradeNo,coupon.getUseOutTradeNo())
                .eq(Coupon::getId, coupon.getId())
                .eq(Coupon::getStatus,0)
                .eq(Coupon::getIsDelete,0)
                .update();

    }

}
