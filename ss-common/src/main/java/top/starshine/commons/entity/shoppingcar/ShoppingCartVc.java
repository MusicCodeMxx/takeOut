package top.starshine.commons.entity.shoppingcar;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * <h3>添加到购物车数据模型</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/28  下午 3:46  周四
 * @Description: hello world
 */
@Setter
@Getter
public class ShoppingCartVc implements java.io.Serializable{

    @Min(value = 1,message = "产品标识不合法")
    @NotNull(message = "产品标识不能为空")
    private Long id;

    //@NotEmpty(message = "产品属性不能为空")
    private String value;

}
