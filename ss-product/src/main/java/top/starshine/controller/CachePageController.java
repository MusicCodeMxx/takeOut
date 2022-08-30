package top.starshine.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import top.starshine.commons.aspect.ApiRestController;
import top.starshine.commons.dubbo.ShoppingCartDubboService;
import top.starshine.commons.entity.product.HomePageCacheVo;
import top.starshine.commons.entity.shoppingcar.ShoppingCartVo;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.handle.ThreadLocalCache;
import top.starshine.service.impl.LoadHomePageCacheService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <h3>加载组合缓存页面</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/27  下午 10:04  周三
 * @Description: hello world
 */
@Slf4j
@RequiredArgsConstructor
@ApiRestController("/page/")
public class CachePageController {

    private final ThreadPoolExecutor threadPoolExecutor;
    private final LoadHomePageCacheService loadHomePageCacheService;

    @DubboReference(cluster = "failback", interfaceClass = ShoppingCartDubboService.class,
            interfaceName = "top.top.starshine.dubbo.ShoppingCartDubboService", version = "1.0.0")
    private ShoppingCartDubboService shoppingCartDubboService;


    // 加载首页数据
    @GetMapping("home")
    public HomePageCacheVo loadHomePageCache() throws ExecutionException, InterruptedException {
        // 开辟新线程, 请求合并, 加载分类信息, 产品信息
        CompletableFuture<HomePageCacheVo> masterThread = CompletableFuture.supplyAsync(loadHomePageCacheService::load, threadPoolExecutor);
        List<ShoppingCartVo> shoppingCartVos = null;
        // 登录用户可以顺便查询购车数据, 这里不适用异步, 远程调用加载购物车需要用户信息
        User user = ThreadLocalCache.get();
        if (null != user) shoppingCartVos = shoppingCartDubboService.list();
        // 阻塞等待分类和产品数据加载完成
        HomePageCacheVo homePageCacheVo = (HomePageCacheVo) CompletableFuture.anyOf(masterThread).get();
        homePageCacheVo.setShoppingCartVos(shoppingCartVos);
        return homePageCacheVo;// 返回前端
    }

}
