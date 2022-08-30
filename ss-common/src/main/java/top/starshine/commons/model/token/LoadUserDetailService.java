package top.starshine.commons.model.token;

import top.starshine.commons.entity.user.User;

/**
 * <h3>令牌加载用户信息</h3>
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/24  下午 7:28  周日
 * @Description: hello world
 */
public interface LoadUserDetailService {

    /**
     * <h1>必须实现</h1>
     * <h3>加载用户信息</h3>
     * @param id {@link String} 用户主键
     * @return {@link User} 用户信息
     */
    User getUserById(String id);

}
