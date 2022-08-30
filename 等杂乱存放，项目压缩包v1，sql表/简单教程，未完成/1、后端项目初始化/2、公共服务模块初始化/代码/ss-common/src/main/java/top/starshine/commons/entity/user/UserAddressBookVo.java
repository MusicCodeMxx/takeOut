package top.starshine.commons.entity.user;

import lombok.Getter;
import lombok.Setter;

/**
 * <h3>地址簿视图对象</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/31  下午 4:21  周日
 * @Description: hello world
 */
@Setter
@Getter
public class UserAddressBookVo implements java.io.Serializable{

    private String id;

    /** 收货人姓名 */
    private String consigneeName;

    /** 手机号 */
    private String phoneNumber;

    /** 性别 0 女 1 男 */
    private Integer sex;

    /** 详细地址 */
    private String detail;

    /** 标签下标,默认,家,学校,公司,其他*/
    private String label;

    /** 是否默认,0否,1是 */
    private Integer isDefault;

}
