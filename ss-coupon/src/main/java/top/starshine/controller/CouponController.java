package top.starshine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import top.starshine.commons.aspect.ApiRestController;
import top.starshine.commons.converter.CouponConverter;
import top.starshine.commons.entity.coupon.CouponVo;
import top.starshine.service.CouponService;

import java.util.List;

/**
 * <h3>优惠券接口</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 2:43  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@RequiredArgsConstructor
@ApiRestController("/coupon/")
public class CouponController {

    private final CouponService couponService;
    private final CouponConverter couponConverter;

    // 获取用户当前持有的优惠券列表
    @GetMapping("list")
    public List<CouponVo> list(){
        return couponConverter.couponsToCouponVos(couponService.meCouponList());
    }

}
