package top.starshine.commons.converter;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import top.starshine.commons.entity.product.*;

import java.util.List;

/**
 * <h3>产品 Bean 转换器</h3>
 * @author: starshine
 * @version: 1.0
 * @since: 2022/7/24  下午 3:55  周日
 * @Description:
 */
@Mapper(componentModel = "spring")
public interface ProductConverter {

    @InheritConfiguration(name = "categoryToCategoryVo")
    List<CategoryVo> categorysToCategoryVos(List<Category> source);

    CategoryVo categoryToCategoryVo(Category source);

    @InheritConfiguration(name = "productToProductVo")
    List<ProductVo> productsToProductVos(List<Product> source);

    ProductVo productToProductVo(Product source);

    AttributesVo attributesToAttributesVo(Attributes source);

    @InheritConfiguration(name = "attributesDtoToAttributesVo")
    List<AttributesVo> attributesDtosToAttributesVos(List<AttributesDto> source);

    AttributesVo attributesDtoToAttributesVo(AttributesDto source);

}
