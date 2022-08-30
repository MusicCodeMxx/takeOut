package top.starshine.commons.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <h3>基础类</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/25  下午 3:56  周一
 * @Description: hello world
 */
@Data // set get tostring hash...
@AllArgsConstructor // 全参构造器
@NoArgsConstructor // 无参构造器
@Accessors(chain = true) // 构建模式
public abstract class BaseEntity implements java.io.Serializable{

    /** 主键,自动注入 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /** 更新人 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateById;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /** 创建人 */
    @TableField(fill = FieldFill.INSERT)
    private Long createById;

    /* 逻辑删除标识(0未删除,1已删除) */
    @TableField(fill = FieldFill.INSERT)
    private Integer isDelete;

}
