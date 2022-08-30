package top.starshine.service;

import top.starshine.commons.entity.shoppingcar.ShoppingCartVo;
import top.starshine.commons.entity.user.User;

import java.util.List;

/**
 * <h3>购物车服务接口</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/28  下午 3:39  周四
 * @Description: hello world
 */
public interface ShoppingCartService  {

    /**
     * 获取购车数据
     * @return 购物车视图对象数组 {@link ShoppingCartVo}
     */
    List<ShoppingCartVo> list();

    /**
     * 增加购物车中的产品数量获取添加产品
     * @param id 产品主键
     * @param value 产品属性
     * @return 操作之后当前产品的数量
     */
    int add(Long id, String value);

    /**
     * 异步清除用户购物车
     * @param user 用户信息
     */
    void clear(User user);

    /**
     * 减少购物车中的产品数量
     * <p>若当前商品数量1,再调用该接口就删除该产品</p>
     * @param id 产品主键
     * @param value 产品属性
     * @return 操作之后当前产品的数量
     */
    int subtract(Long id, String value);


}
