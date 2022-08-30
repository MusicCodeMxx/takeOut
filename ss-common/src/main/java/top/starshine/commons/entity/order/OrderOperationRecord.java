package top.starshine.commons.entity.order;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.starshine.commons.enums.OrderOperationRecordTypeEnum;

import java.util.Date;

/**
 * <h3>订单操作历史</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  上午 1:26  周一
 * @Description: hello world
 */
@Data // set get tostring hash...
@AllArgsConstructor // 全参构造器
@NoArgsConstructor // 无参构造器
@Accessors(chain = true) // 构建模式
@TableName("ss_order_operation_record") // 用户表
public class OrderOperationRecord {

    /** 主键,自动注入 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 绑定主订单主键 */
    private Long orderId;

    /** 订单状态改变前状态 */
    private Integer beforeStatus;

    /** 订单状态改变之后状态 */
    private Integer afterStatus;

    /** 注释或备注 */
    private String note;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 查看这个枚举
     * {@link OrderOperationRecordTypeEnum}
     * */
    private String type;

}
