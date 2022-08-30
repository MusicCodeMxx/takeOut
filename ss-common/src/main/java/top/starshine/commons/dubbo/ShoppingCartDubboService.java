package top.starshine.commons.dubbo;

import top.starshine.commons.entity.shoppingcar.ShoppingCartVo;

import java.util.List;

/**
 * <h3>购物车 RPC 调用</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/29  下午 3:19  周五
 * @Description: hello world
 */
public interface ShoppingCartDubboService {

    /**
     * 获取当前登录用户的购车数据
     * @return 购物车视图对象数组 {@link ShoppingCartVo}
     */
    List<ShoppingCartVo> list();

    /**
     * 移除登录用户的购物车缓存
     */
    void clear();
}
