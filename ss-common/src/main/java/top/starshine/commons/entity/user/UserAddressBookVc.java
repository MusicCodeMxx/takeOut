package top.starshine.commons.entity.user;

import lombok.Getter;
import lombok.Setter;
import top.starshine.commons.model.validation.IsPhoneNumber;

import javax.validation.constraints.NotEmpty;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/31  下午 4:38  周日
 * @Description: hello world
 */
@Setter
@Getter
public class UserAddressBookVc implements java.io.Serializable{

    /** 收货人姓名 */
    @NotEmpty(message = "收货人姓名不能为空")
    private String consigneeName;

    /** 手机号 */
    @IsPhoneNumber
    @NotEmpty(message = "手机号")
    private String phoneNumber;

    /** 详细地址 */
    @NotEmpty(message = "详细地址不能为空")
    private String detail;

    /** 性别,0女,1男 */
    private Integer sex;

    /** 标签下标,0默认,1家,2学校,3公司,4其他*/
    private String label;

    /** 是否默认,0否,1是 */
    private Integer isDefault;

}
