package top.starshine.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.starshine.commons.aspect.ApiRestController;
import top.starshine.commons.converter.ProductConverter;
import top.starshine.commons.entity.product.AttributesDto;
import top.starshine.commons.entity.product.Product;
import top.starshine.commons.entity.product.ProductVo;
import top.starshine.commons.util.CollectionUtils;
import top.starshine.service.AttributesService;
import top.starshine.service.ProductService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/27  下午 10:02  周三
 * @Description: hello world
 */
@Slf4j
@RequiredArgsConstructor
@ApiRestController("/product/")
public class ProductController {

    private final ProductService productService;
    private final ProductConverter productConverter;
    private final AttributesService attributesService;

    @GetMapping("/list/by/category")
    public List<ProductVo> listByCategoryId(@RequestParam("id") Long id){
        // TODO 缓存
        List<Product> products = productService.listByCategoryId(id);

        // 找出主键, 查询出对应的产品属性
        Set<Long> productIds = new HashSet<>(products.size());
        for (Product product : products) {
            productIds.add(product.getId());
        }
        List<AttributesDto> attributesPos = attributesService.findProductPropertiesByIds(productIds);
        productIds = null;

        // 查询产品信息对应属性
        List<ProductVo> productVos = productConverter.productsToProductVos(products);
        products = null;
        if(CollectionUtils.isEmpty(productVos)) return null;

        // 组装到对应的产品属性中去
        for (ProductVo productVo : productVos) {
            // 过滤出该产品的属性,并且填充到产品属性中
            productVo.setAttributesVos(productConverter.attributesDtosToAttributesVos(
                    attributesPos.stream().filter(ad -> ad.getProductId().equals(Long.valueOf(productVo.getId()))).collect(Collectors.toList())));
        }
        return productVos;
    }

}
