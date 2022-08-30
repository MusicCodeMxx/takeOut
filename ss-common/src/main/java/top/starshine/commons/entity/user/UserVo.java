package top.starshine.commons.entity.user;

import lombok.Getter;
import lombok.Setter;

/**
 * <h3>用户视图对象</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/25  下午 3:52  周一
 * @Description: hello world
 */
@Setter
@Getter
public class UserVo implements java.io.Serializable{

    /**主键*/
    private String id;

    /**头像*/
    private String avatar;

    /**用户昵称*/
    private String nickname;

    /**手机号*/
    private String phoneNumber;

    /**描述*/
    private String description;

    /**邮箱*/
    private String email;

    /**性别,0女,1男*/
    private Integer sex;

}
