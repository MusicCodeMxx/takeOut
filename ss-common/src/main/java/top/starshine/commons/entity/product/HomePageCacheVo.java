package top.starshine.commons.entity.product;

import lombok.Getter;
import lombok.Setter;
import top.starshine.commons.entity.shoppingcar.ShoppingCartVo;

import java.util.List;

/**
 * <h3>首页视图数据</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/28  下午 1:16  周四
 * @Description: hello world
 */
@Setter
@Getter
public class HomePageCacheVo implements java.io.Serializable{

    /**分类*/
    private List<CategoryVo> categoryVos;

    /**产品数据*/
    private List<ProductVo> productVos;

    /**购车数据*/
    private List<ShoppingCartVo> shoppingCartVos;

}


