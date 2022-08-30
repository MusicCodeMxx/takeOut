package top.starshine.commons.entity.user;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.starshine.commons.entity.BaseEntity;

/**
 * 用户地址簿
 */
@Data // set get tostring hash...
@AllArgsConstructor // 全参构造器
@NoArgsConstructor // 无参构造器
@Accessors(chain = true) // 构建模式
@TableName("ss_user_address_book")
@EqualsAndHashCode(callSuper = true) // 继承需要操作父类校验之类
public class UserAddressBook extends BaseEntity implements java.io.Serializable {

    /** 绑定用户主键 */
    private Long userId;
	
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