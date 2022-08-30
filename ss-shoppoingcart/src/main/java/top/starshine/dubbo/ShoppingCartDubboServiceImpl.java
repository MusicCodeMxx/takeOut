package top.starshine.dubbo;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import top.starshine.commons.dubbo.ShoppingCartDubboService;
import top.starshine.commons.entity.shoppingcar.ShoppingCartVo;
import top.starshine.commons.handle.ThreadLocalCache;
import top.starshine.service.ShoppingCartService;

import java.util.List;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/29  下午 4:02  周五
 * @Description: hello world
 */
@DubboService(
        // 集群容错模式
        cluster = "failback",
        // 服务降级
        mock = "return null",
        // 接口类型
        interfaceClass = ShoppingCartDubboService.class,
        // 接口名称
        interfaceName = "top.top.starshine.dubbo.ShoppingCartDubboService",
        // 接口版本
        version = "1.0.0"
)
@RequiredArgsConstructor
public class ShoppingCartDubboServiceImpl implements ShoppingCartDubboService {

    private final ShoppingCartService shoppingCartService;

    @Override
    public List<ShoppingCartVo> list() {
        return shoppingCartService.list();
    }

    @Override
    public void clear() {
        shoppingCartService.clear(ThreadLocalCache.getNotNull());
    }

}
