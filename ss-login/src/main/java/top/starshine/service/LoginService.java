package top.starshine.service;

import top.starshine.commons.entity.user.User;

/**
 * <h3>登录接口</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/26  下午 4:37  周二
 * @Description: hello world
 */
public interface LoginService {

    void sendMsm(String phoneNumber);

    User submit(String phoneNumber, Integer verificationCode);

    void logout(User user);
}
