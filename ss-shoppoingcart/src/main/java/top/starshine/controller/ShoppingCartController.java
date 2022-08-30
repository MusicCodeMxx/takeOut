package top.starshine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.starshine.commons.aspect.ApiRestController;
import top.starshine.commons.aspect.PreventDuplicateSubmissions;
import top.starshine.commons.aspect.RedissonTryLock;
import top.starshine.commons.entity.shoppingcar.ShoppingCartVc;
import top.starshine.commons.entity.shoppingcar.ShoppingCartVo;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.handle.ThreadLocalCache;
import top.starshine.service.ShoppingCartService;

import java.util.List;

/**
 * <h3>购物车控制层</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/28  下午 3:37  周四
 * @Description: hello world
 */
@RequiredArgsConstructor
@ApiRestController("/shoppingCart/")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    // 获取购物车列表
    @GetMapping("list")
    public List<ShoppingCartVo> list() {
        return shoppingCartService.list();
    }

    // 添加产品至购物车
    @RedissonTryLock
    @PutMapping("add")
    public int add(@RequestBody @Validated ShoppingCartVc shoppingCarVc) {
        return shoppingCartService.add(shoppingCarVc.getId(),shoppingCarVc.getValue());
    }

    // 减少购物车的产品数量
    @RedissonTryLock
    @PutMapping("subtract")
    public int subtract(@RequestBody @Validated ShoppingCartVc shoppingCarVc){
        return shoppingCartService.subtract(shoppingCarVc.getId(),shoppingCarVc.getValue());
    }

    // 清除购物车
    @RedissonTryLock
    @DeleteMapping("clear")
    @PreventDuplicateSubmissions
    public void clear(){
        User user = ThreadLocalCache.getNotNull();
        shoppingCartService.clear(user);
    }

}
