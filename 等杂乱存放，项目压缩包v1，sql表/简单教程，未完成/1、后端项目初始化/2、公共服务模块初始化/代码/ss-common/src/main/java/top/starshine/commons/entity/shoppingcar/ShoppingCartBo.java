package top.starshine.commons.entity.shoppingcar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/28  下午 5:03  周四
 * @Description: hello world
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartBo implements java.io.Serializable{

    /** 产品主键 */
    private Long id;

    /** 产品属性 */
    private String value;

    /** 产品数量 */
    private Integer amount;

}
