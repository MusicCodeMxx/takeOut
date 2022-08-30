package top.starshine.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.starshine.commons.entity.product.Product;
import top.starshine.mapper.ProductMapper;
import top.starshine.service.ProductService;

import java.util.List;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/27  下午 10:00  周三
 * @Description: hello world
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public List<Product> listByCategoryId(Long categoryId) {
        return new LambdaQueryChainWrapper<>(super.getBaseMapper())
                .eq(Product::getCategoryId,categoryId)
                .eq(Product::getStatus,1)
                .orderByAsc(Product::getSort)
                .select(Product::getId,
                        Product::getCategoryId,
                        Product::getName,
                        Product::getImageDefUrl,
                        Product::getDescription,
                        Product::getPrice)
                .list();
    }


}
