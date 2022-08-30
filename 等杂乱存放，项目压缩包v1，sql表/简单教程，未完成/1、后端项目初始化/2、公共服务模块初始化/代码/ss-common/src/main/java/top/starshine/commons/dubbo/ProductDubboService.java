package top.starshine.commons.dubbo;

import top.starshine.commons.entity.product.Product;

import java.util.List;
import java.util.Set;

/**
 * <h3>产品服务远程调用</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/28  下午 10:51  周四
 * @Description: hello world
 */
public interface ProductDubboService {


    List<Product> findByIds(Set<String> keys);

}

