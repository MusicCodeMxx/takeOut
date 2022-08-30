package top.starshine.commons.entity.user;

import lombok.Getter;
import lombok.Setter;
import top.starshine.commons.model.validation.IsPhoneNumber;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <h3>用户修改地址提交对象</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/5  下午 6:25  周五
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Setter
@Getter
public class UserAddressBookUpdateVc implements java.io.Serializable{

    /**主键*/
    @NotNull(message = "地址绑定标识不能为空")
    private Long id;

    /** 收货人姓名 */
    private String consigneeName;

    /** 手机号 */
    @IsPhoneNumber
    private String phoneNumber;

    /** 详细地址 */
    private String detail;

    /** 性别,0女,1男 */
    private Integer sex;

    /** 标签下标,0默认,1家,2学校,3公司,4其他*/
    private String label;

    /** 是否默认,0否,1是 */
    private Integer isDefault;


}
