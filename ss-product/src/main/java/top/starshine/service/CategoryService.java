package top.starshine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.starshine.commons.entity.product.Category;

import java.util.List;

/**
 * <h3>分类服务</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/27  下午 9:54  周三
 * @Description: hello world
 */
public interface CategoryService extends IService<Category> {

    List<Category> queryList();

}
