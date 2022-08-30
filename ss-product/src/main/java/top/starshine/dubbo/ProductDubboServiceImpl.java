package top.starshine.dubbo;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import top.starshine.commons.dubbo.ProductDubboService;
import top.starshine.commons.entity.product.Product;
import top.starshine.service.ProductService;

import java.util.List;
import java.util.Set;

/**
 * <h3> RPC 远程调用接口</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/29  下午 12:50  周五
 * @Description: hello world
 */
@DubboService(
        // 集群容错模式
        cluster = "failback",
        // 服务降级
        mock = "return null",
        // 接口类型
        interfaceClass = ProductDubboService.class,
        // 接口名称
        interfaceName = "top.starshine.dubbo.ProductDubboService",
        // 接口版本
        version = "1.0.0"
)
@RequiredArgsConstructor
public class ProductDubboServiceImpl implements ProductDubboService {

    private final ProductService productService;

    @Override
    public List<Product> findByIds(Set<String> keys) {
        return new LambdaQueryChainWrapper<>(productService.getBaseMapper())
                .in(Product::getId,keys)
                .select(Product::getId,
                        Product::getCategoryId,
                        Product::getName,
                        Product::getImageDefUrl,
                        Product::getPrice)
                .list();
    }

}
