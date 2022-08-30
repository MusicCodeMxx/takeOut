package top.starshine.commons.entity.order;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.starshine.commons.entity.BaseEntity;

/**
 * <h3>子订单详情信息</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  上午 12:18  周一
 * @Description: hello world
 */
@Data // set get tostring hash...
@AllArgsConstructor // 全参构造器
@NoArgsConstructor // 无参构造器
@Accessors(chain = true) // 构建模式
@TableName("ss_child_order_detail") // 用户表
@EqualsAndHashCode(callSuper = true) // 继承需要操作父类校验之类
public class ChildOrderDetail extends BaseEntity implements java.io.Serializable {

    /** 绑定主订单主键 */
    private Long orderId;

    /**绑定用户主键*/
    //private Long userId;

    /** 产品主键 */
    private Long productId;

    /** 订单号 */
    private String outTradeNo;

    /**产品归属分类*/
    private Long categoryId;

    /** 产品名 */
    private String name;

    /** 产品属性 > 口味 */
    private String value;

    /** 产品主图片 */
    private String imageDefUrl;

    /** 产品价格 */
    private Double price;

    /** 产品购买数量 */
    private Integer amount;

    /** 订单原始价 */
    private Double originalPrice;


}
