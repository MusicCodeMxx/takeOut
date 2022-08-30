package top.starshine.commons.entity.login;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户登录记录表(LoginRecord)表实体类
 *
 * @author makejava
 * @since 2022-06-23 18:20:48
 */
@Data // set get tostring hash...
@AllArgsConstructor // 全参构造器
@NoArgsConstructor // 无参构造器
@Accessors(chain = true) // 构建模式
@TableName("ss_login_record")
public class LoginRecord implements java.io.Serializable {

    /** 绑定用户主键ID */
    private Long userId;

    /** 枚举退登录类型 */
    private String type;

    /** 登录的IP地址 */
    private String ip;

    /** 登录IP归属地址 */
    private String address;

    /** 省级 */
    private String proCode;

    /** 市级 */
    private String cityCode;

    /** 登录的浏览器名 */
    private String browserName;

    /** 登录的浏览器版本 */
    private String browserVersion;

    /** 登录的浏览器生产厂商 */
    private String manufacturer;

    /** 登录设备类型 */
    private String deviceType;

    /** 登录操作系统名 */
    private String systemName;

    /** 登录设备的操作系统家族 */
    private String systemGroup;

    /** 主键,自动注入 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}

