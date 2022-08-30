package top.starshine.commons.entity.product;

import lombok.Getter;
import lombok.Setter;

/**
 * <h3>产品属性数据库映射对象</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/28  下午 3:20  周四
 * @Description: hello world
 */
@Setter
@Getter
public class AttributesDto implements java.io.Serializable{

    /** 属性主键 */
    private Long id;

    /** 产品主键 */
    private Long productId;

    /** 属性名 */
    private String name;

    /** 属性值 */
    private String value;

    /** 默认选择的属性值下标,-1表示没有默认值 */
    private Integer defValueIndex;

    /** 属性描述 */
    private String description;

}
