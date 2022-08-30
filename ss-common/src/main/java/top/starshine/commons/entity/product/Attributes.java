package top.starshine.commons.entity.product;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.starshine.commons.entity.BaseEntity;

/**
 * <h3>产品属性</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/27  下午 9:59  周三
 * @Description: hello world
 */
@Data // set get tostring hash...
@AllArgsConstructor // 全参构造器
@NoArgsConstructor // 无参构造器
@Accessors(chain = true) // 构建模式
@TableName("ss_attributes")
@EqualsAndHashCode(callSuper = true) // 继承需要操作父类校验之类
public class Attributes extends BaseEntity implements java.io.Serializable{

    /** 属性名称 */
    private String name;

    /***口味数据数组:[属性1,属性2,...] */
    private String value;

    /** 默认选择的属性值下标,-1表示没有默认值 */
    private Integer defValueIndex;

    /** 属性描述 */
    private String description;

    /** 顺序,越大排序最前 */
    private Integer sort;

}
