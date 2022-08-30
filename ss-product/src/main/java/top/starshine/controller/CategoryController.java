package top.starshine.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import top.starshine.commons.aspect.ApiRestController;
import top.starshine.commons.converter.ProductConverter;
import top.starshine.commons.entity.product.Category;
import top.starshine.commons.entity.product.CategoryVo;
import top.starshine.commons.util.CollectionUtils;
import top.starshine.service.CategoryService;

import java.util.List;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/27  下午 9:53  周三
 * @Description: hello world
 */
@Slf4j
@RequiredArgsConstructor
@ApiRestController("/category/")
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductConverter productConverter;

    // 获取分类列表
    @GetMapping("list")
    public List<CategoryVo> list(){
        List<Category> categories = categoryService.queryList();
        if (CollectionUtils.isEmpty(categories)) return null;
        return productConverter.categorysToCategoryVos(categories);
    }

}
