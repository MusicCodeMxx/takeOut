package top.starshine.commons.entity.product;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.starshine.commons.entity.BaseEntity;

/**
 * <h3>产品信息</h3>
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
@TableName("ss_product")
@EqualsAndHashCode(callSuper = true) // 继承需要操作父类校验之类
public class Product extends BaseEntity implements java.io.Serializable{

    /** 顺序,越大排序最前 */
    private Integer sort;

    /** 绑定分类主键 */
    private Long categoryId;

    /** 产品名称 */
    private String name;

    /** 描述 */
    private String description;

    /** 产品主图片 */
    private String imageDefUrl;

    /** 产品价格 */
    private Double price;

    /** 产品状态,0未上架,1已上架 */
    private Integer status;

}
