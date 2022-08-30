package top.starshine.commons.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户持久层对象映射
 * @author: starshine
 * @version: 1.0
 * @since: 2022/6/22  下午 5:19  周三
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("user")
public class User  implements java.io.Serializable {

    /** 头像 */
    private String avatar;

    /** 用户昵称 */
    private String nickname;

    /** 手机号 */
    private String phoneNumber;

    /** 描述 */
    private String description;

    /** 邮箱 */
    private String email;

    /** 会员等级,0 普通会员, 1尊贵会员*/
    private Integer rank;

    /** 性别 0 女 1 男 */
    private Integer sex;

    /** 状态 0:正常,1:禁用 */
    private Integer status;

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
