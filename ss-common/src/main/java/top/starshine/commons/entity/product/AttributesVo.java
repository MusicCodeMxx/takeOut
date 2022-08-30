package top.starshine.commons.entity.product;

import lombok.Getter;
import lombok.Setter;

/**
 * <h3>产品属性视图对象</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/27  下午 10:21  周三
 * @Description: hello world
 */
@Setter
@Getter
public class AttributesVo implements java.io.Serializable {

    /** 主键,自动注入 */
    private String id;

    /** 属性名称 */
    private String name;

    /*** 口味数据数组:[属性1,属性2,...] */
    private String value;

    /** 默认选择的属性值下标,-1表示没有默认值 */
    private Integer defValueIndex;

    /** 属性描述 */
    private String description;

}