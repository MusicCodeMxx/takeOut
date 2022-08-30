package top.starshine.commons.dubbo;

import top.starshine.commons.entity.user.User;
import top.starshine.commons.entity.user.UserAddressBookVo;

/**
 * <h3>用户微服务 Dubbo 远程调用</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/26  下午 4:26  周二
 * @Description: hello world
 */
public interface UserDubboService {

    /**
     * <h3>通过手机号查找出用户信息</h3>
     * @param phoneNumber {@link String} 手机号
     * @return {@link User} 用户信息
     */
    User findUserByPhoneNumber(String phoneNumber);

    /**
     * <h3>通过主键查询出用户信息</h3>
     * @param id {@link String} 用户主键
     * @return {@link User} 用户信息
     */
    User findUserById(String id);

    /**
     * 保存用户信息
     * @param user {@link User} 用户对象
     */
    void saveUserDetail(User user);

    /**
     * 获取用户地址簿中的默认地址
     * @return 地址信息
     */
    UserAddressBookVo getDefaultUserAddress();

}
