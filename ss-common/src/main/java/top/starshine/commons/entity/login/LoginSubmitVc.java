package top.starshine.commons.entity.login;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.starshine.commons.model.validation.IsPhoneNumber;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <h3>登录信息</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/26  下午 11:02  周二
 * @Description: hello world
 */
@Setter
@Getter
@ToString
public class LoginSubmitVc implements java.io.Serializable{

    @IsPhoneNumber
    @NotEmpty(message = "手机号不能为空")
    private String phoneNumber;

    @NotNull(message = "验证码不能为空")
    private Integer verificationCode;

}
