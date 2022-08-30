package top.starshine.commons.entity.product;

import lombok.Getter;
import lombok.Setter;

/**
 * <h3>产品分类视图对象</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/27  下午 10:18  周三
 * @Description: hello world
 */
@Setter
@Getter
public class CategoryVo implements java.io.Serializable{

    /** 主键,自动注入 */
    private String id;

    /** 类型   1 菜品分类 2 套餐分类 */
    private Integer type;

    /** 描述 */
    private String description;

    /** 分类名称   */
    private String name;

}
