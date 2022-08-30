package top.starshine.controller;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import top.starshine.commons.aspect.ApiRestController;
import top.starshine.commons.aspect.PreventDuplicateSubmissions;
import top.starshine.commons.dubbo.CouponDubboService;
import top.starshine.commons.dubbo.ProductDubboService;
import top.starshine.commons.dubbo.ShoppingCartDubboService;
import top.starshine.commons.dubbo.UserDubboService;
import top.starshine.commons.entity.order.BuildBillDataVo;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.handle.ThreadLocalCache;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <h3>聚合接口</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/31  下午 10:54  周日
 * @Description: hello world
 */
@RequiredArgsConstructor
@ApiRestController("/build/")
public class BuildOrderController {

    private final ThreadPoolExecutor threadPoolExecutor;

    @DubboReference(cluster = "failback", interfaceClass = ProductDubboService.class,
            interfaceName = "top.top.starshine.dubbo.ProductDubboService", version = "1.0.0")
    private ProductDubboService productDubboService;

    @DubboReference(cluster = "failback", interfaceClass = ShoppingCartDubboService.class,
            interfaceName = "top.top.starshine.dubbo.ShoppingCartDubboService", version = "1.0.0")
    private ShoppingCartDubboService shoppingCartDubboService;

    @DubboReference(cluster = "failback", interfaceClass = UserDubboService.class,
            interfaceName = "top.top.starshine.dubbo.UserDubboService", version = "1.0.0")
    private UserDubboService userDubboService;

    @DubboReference(cluster = "failback", interfaceClass = CouponDubboService.class,
            interfaceName = "top.top.starshine.dubbo.CouponDubboService", version = "1.0.0")
    private CouponDubboService couponDubboService;

    // build/billData

    // 聚合要构建账单数据给前端
    @GetMapping("billData")
    @PreventDuplicateSubmissions(value = 1)
    public BuildBillDataVo loadBillData() {
        User user = ThreadLocalCache.getNotNull();
        BuildBillDataVo buildBillDataVo = new BuildBillDataVo();

        // 异步线程, 获取购车数据
        CompletableFuture<Void> futureOne = CompletableFuture.runAsync(() -> {
            ThreadLocalCache.put(user);
            try {
                buildBillDataVo.setShoppingCartVos(shoppingCartDubboService.list());
            } finally {
                ThreadLocalCache.remove();
            }
        }, threadPoolExecutor);

        // 异步线程, 获取默认地址簿
        CompletableFuture<Void> futureTwo = CompletableFuture.runAsync(() -> {
            ThreadLocalCache.put(user);
            try {
                buildBillDataVo.setUserAddressBookVo(userDubboService.getDefaultUserAddress());
            } finally {
                ThreadLocalCache.remove();
            }
        }, threadPoolExecutor);

        try {
            // 使用本线程同步, 获取优惠券列表
            buildBillDataVo.setCouponVos(couponDubboService.getUserCouponList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // 阻塞等待完成  两个线程都执行成功了，才返回null，否则报异常
            CompletableFuture.allOf(futureOne, futureTwo).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // 返回前端
        return buildBillDataVo;
    }



}
