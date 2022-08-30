package top.starshine.commons.converter;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import top.starshine.commons.entity.product.Product;
import top.starshine.commons.entity.shoppingcar.ShoppingCartBo;
import top.starshine.commons.entity.shoppingcar.ShoppingCartVo;

import java.util.List;

/**
 * <h3>购物车服务 Bean 转换对器</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/28  下午 10:55  周四
 * @Description: hello world
 */
@Mapper(componentModel = "spring")
public interface ShoppingCartConverter {

    @InheritConfiguration(name = "shoppingCartBoToShoppingCartVo")
    List<ShoppingCartVo> shoppingCartBosToShoppingCartVos(List<ShoppingCartBo> source);

    ShoppingCartVo shoppingCartBoToShoppingCartVo(ShoppingCartBo source);

    @Mapping(target = "id", ignore = true)
    void copyProductToShoppingCartVo(Product source, @MappingTarget ShoppingCartVo target);

}
