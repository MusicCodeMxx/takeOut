package top.starshine.commons.entity.user;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.starshine.commons.entity.BaseEntity;

/**
 * 用户持久层对象映射
 * @author: starshine
 * @version: 1.0
 * @since: 2022/6/22  下午 5:19  周三
 * @Description:
 */
@Data // set get tostring hash...
@AllArgsConstructor // 全参构造器
@NoArgsConstructor // 无参构造器
@Accessors(chain = true) // 构建模式
@TableName("ss_user") // 用户表
@EqualsAndHashCode(callSuper = true) // 继承需要操作父类校验之类
public class User extends BaseEntity implements java.io.Serializable {

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

    /** 性别 0 女 1 男 */
    private Integer sex;

    /** 状态 0:正常,1:禁用 */
    private Integer status;

}
