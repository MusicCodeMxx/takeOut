package top.starshine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.starshine.commons.entity.order.OrderOperationRecord;
import top.starshine.mapper.OrderOperationRecordMapper;
import top.starshine.service.OrderOperationRecordService;

/**
 * <h3>订单操作记录服务接口实现类</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/3  下午 5:42  周三
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Service
public class OrderOperationRecordServiceImpl extends ServiceImpl<OrderOperationRecordMapper,OrderOperationRecord> implements OrderOperationRecordService {
}
