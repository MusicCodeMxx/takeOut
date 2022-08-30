package top.starshine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.starshine.commons.aspect.ApiRestController;
import top.starshine.commons.aspect.PreventDuplicateSubmissions;
import top.starshine.commons.aspect.RedissonTryLock;
import top.starshine.commons.converter.CouponConverter;
import top.starshine.commons.entity.coupon.Coupon;
import top.starshine.commons.entity.coupon.CouponVo;
import top.starshine.commons.handle.ThreadLocalCache;
import top.starshine.commons.util.CollectionUtils;
import top.starshine.service.CouponBatchDetailService;
import top.starshine.service.CouponService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 2:44  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@RequiredArgsConstructor
@ApiRestController("/couponBatch/")
public class CouponBatchController {

    private final CouponService couponService;
    private final CouponConverter couponConverter;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final CouponBatchDetailService couponBatchDetailService;

    // 获取可以领取优惠券列表
    @GetMapping("list")
    public List<CouponVo> list(){
        // 使用异步获取可以领取的优惠券
        CompletableFuture<List<CouponVo>> future = CompletableFuture.supplyAsync(() ->
                couponConverter.couponBatchDetailsToCouponVos(couponBatchDetailService.findList()), threadPoolExecutor);
        // 获取已领取的优惠券
        List<Coupon> coupons = null;
        if (ThreadLocalCache.isNotNull()) coupons = couponService.selectIdAndBatchId();

        // 阻塞等待完成
        List<CouponVo> couponVos = null;
        try {
            couponVos = future.get();
        } catch (InterruptedException | ExecutionException ignored) {
        } finally {
            future = null;
        }
        // 合并填充
        if (CollectionUtils.isEmpty(couponVos)) return null;
        if (CollectionUtils.isNotEmpty(coupons)){
            for (Coupon coupon : coupons) {
                for (CouponVo couponVo : couponVos) {
                    if (coupon.getBatchId().equals(Long.valueOf(couponVo.getBatchId()))){
                        couponVo.setId(String.valueOf(coupon.getId()));
                    }
                }
            }
            coupons = null;
        }
        // 返回前端
        return couponVos;
    }

    // 领取的优惠券
    @RedissonTryLock
    @PreventDuplicateSubmissions(value = 1)
    @GetMapping("receive/{id}")
    public Long receiveCouponById(@PathVariable("id")Long id){
        return couponBatchDetailService.receiveCouponById(id);
    }

}

