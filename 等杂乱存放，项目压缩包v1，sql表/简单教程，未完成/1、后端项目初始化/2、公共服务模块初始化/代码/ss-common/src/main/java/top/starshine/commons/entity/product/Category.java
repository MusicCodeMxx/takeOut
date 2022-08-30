package top.starshine.commons.entity.product;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.starshine.commons.entity.BaseEntity;

/**
 * <h3>产品分类</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/27  下午 9:55  周三
 * @Description: hello world
 */
@Data // set get tostring hash...
@AllArgsConstructor // 全参构造器
@NoArgsConstructor // 无参构造器
@Accessors(chain = true) // 构建模式
@TableName("ss_category") // 用户表
@EqualsAndHashCode(callSuper = true) // 继承需要操作父类校验之类
public class Category extends BaseEntity implements java.io.Serializable{

    /** 类型:1菜品分类,2套餐分类 */
    private Integer type;

    /** 描述 */
    private String description;

    /** 分类名称   */
    private String name;

    /*** 顺序,越大排序最前 */
    private Integer sort;

}
