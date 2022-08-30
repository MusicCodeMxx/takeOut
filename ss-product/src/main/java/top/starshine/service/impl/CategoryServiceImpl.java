package top.starshine.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.starshine.commons.entity.product.Category;
import top.starshine.mapper.CategoryMapper;
import top.starshine.service.CategoryService;

import java.util.List;

/**
 * <h3>分类服务实现类</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/27  下午 9:58  周三
 * @Description: hello world
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<Category> queryList() {
        return new LambdaQueryChainWrapper<>(getBaseMapper())
                .eq(Category::getIsDelete,0)
                .orderByDesc(Category::getSort)
                .list();
    }

}
