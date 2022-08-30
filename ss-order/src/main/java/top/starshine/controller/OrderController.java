package top.starshine.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.starshine.commons.aspect.ApiRestController;
import top.starshine.commons.aspect.PreventDuplicateSubmissions;
import top.starshine.commons.aspect.RedissonLock;
import top.starshine.commons.entity.order.OrderDetailVo;
import top.starshine.commons.entity.order.SubmitOrderVC;
import top.starshine.commons.entity.order.TradeOrderDto;
import top.starshine.commons.exception.BadRequestException;
import top.starshine.commons.status.OrderStatus;
import top.starshine.service.OrderDetailService;
import top.starshine.service.TradeBuilderService;

/**
 * <h3>订单 web 控制层</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/25  上午 12:12  周一
 * @Description: hello world
 */
@RequiredArgsConstructor
@ApiRestController("/order/")
public class OrderController {

    private final OrderDetailService orderDetailService;
    private final TradeBuilderService tradeBuilderService;

    // 提交订单, 下单
    @PostMapping("submit")
    @PreventDuplicateSubmissions(value = 1)
    @RedissonLock("lock:aop:order:submit:user:id:")
    public String submit(@RequestBody @Validated SubmitOrderVC submitOrderVC){
        return tradeBuilderService.createTrade(new TradeOrderDto(submitOrderVC));
    }

    // 获取分页订单列表,current 当前页, size 页容量
    // 每页 3 条数据, 因数据比较多
    @GetMapping("pageList")
    @PreventDuplicateSubmissions(value = 3)
    public IPage<OrderDetailVo> pageList(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "3") long size) {
        return orderDetailService.pageList(current,size);
    }

    // 取消指定的订单
    @PutMapping("cancel/by/{id}")
    @PreventDuplicateSubmissions(value = 1)
    public void cancelById(@PathVariable("id") String id){
        if (!StringUtils.hasText(id)) throw new BadRequestException(OrderStatus.ORDER_ID_IS_EMPTY);
        orderDetailService.cancelById(id);
    }

    // 删除订单
    @DeleteMapping("delete/by/{id}")
    public void delete(@PathVariable("id") String id){
        if (!StringUtils.hasText(id)) throw new BadRequestException(OrderStatus.ORDER_ID_IS_EMPTY);
        orderDetailService.deleteById(id);
    }
}
