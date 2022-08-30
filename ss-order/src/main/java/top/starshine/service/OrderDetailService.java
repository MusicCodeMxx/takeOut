package top.starshine.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import top.starshine.commons.entity.order.OrderDetail;
import top.starshine.commons.entity.order.OrderDetailVo;
import top.starshine.commons.entity.order.TradeOrderDto;

/**
 * <h3>订单服务接口</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/2  下午 9:19  周二
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
public interface OrderDetailService extends IService<OrderDetail> {

    /**
     * 超时自动取消订单, 状态为系统超时自动取消订单
     * @param orderDetails 订单详情对象
     */
    boolean cancel(OrderDetail orderDetails);

    /**
     * 保存贸易构建数据, 订单生成的数据保存
     * @param tod 数据体
     */
    void saveTradeBuildData(TradeOrderDto tod);

    /**
     * 订单列表分页
     * @param current 当前页
     * @param size 分页容量
     * @return 分页对象
     */
    IPage<OrderDetailVo> pageList(long current, long size);

    /**
     * 取消指定订单
     * @param id 订单主键
     */
    void cancelById(String id);

    /**
     * 通过主键, 删除符合状态的订单
     * @param id 订单主键
     */
    void deleteById(String id);
}
