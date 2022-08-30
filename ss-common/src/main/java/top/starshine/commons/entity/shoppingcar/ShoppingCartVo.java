package top.starshine.commons.entity.shoppingcar;

import lombok.Getter;
import lombok.Setter;

/**
 * <h3>购车产品视图对象</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/28  下午 10:36  周四
 * @Description: hello world
 */
@Setter
@Getter
public class ShoppingCartVo implements java.io.Serializable{

    /** 产品主键 */
    private String id;

    /** 产品属性 */
    private String value;

    /** 绑定分类主键 */
    private String categoryId;

    /** 产品名称 */
    private String name;

    /** 产品主图片 */
    private String imageDefUrl;

    /** 产品价格 */
    private Double price;

    /** 产品数量 */
    private Integer amount;

}
