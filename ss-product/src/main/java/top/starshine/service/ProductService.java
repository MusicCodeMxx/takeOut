package top.starshine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.starshine.commons.entity.product.Product;

import java.util.List;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/27  下午 9:59  周三
 * @Description: hello world
 */
public interface ProductService extends IService<Product> {

    /**
     * 查询归属这个分类的产品集合
     * @param categoryId {@link Long} 分类主键
     * @return 产品集合 {@see Product}
     */
    List<Product> listByCategoryId(Long categoryId);

}
