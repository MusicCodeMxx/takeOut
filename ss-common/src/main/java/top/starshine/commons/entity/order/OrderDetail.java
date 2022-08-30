package top.starshine.commons.entity.order;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.starshine.commons.entity.BaseEntity;

/**
 * <h3>订单详情信息</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  上午 12:10  周一
 * @Description: hello world
 */
@Data // set get tostring hash...
@AllArgsConstructor // 全参构造器
@NoArgsConstructor // 无参构造器
@Accessors(chain = true) // 构建模式
@TableName("ss_order_detail") // 用户表
@EqualsAndHashCode(callSuper = true) // 继承需要操作父类校验之类
public class OrderDetail extends BaseEntity implements java.io.Serializable {

    // ============== 订单流程状态

    /**
     * 订单状态:
     *      0创建订单,
     *      1待付款,
     *      2付款成功,
     *      3制作,
     *      4派送,
     *      5派送完成,
     *      6订单完成(),
     *      7已取消,
     *      8超时取消,
     *      9退款申请,
     *      10退款成功
    */
    private Integer status;

    // ============= 订单价格

    /**订单原始价*/
    private Double originalPrice;

    /**运费*/
    private Double freightPrice;

    /**优惠要减免总价*/
    private Double couponPrice;

    /** 该订单产品总数 */
    private Integer productTotalNumber;

    /** 最终用户需要支付的金额 */
    private Double billPrice;

    // ============= 订单信息

    /** 订单号 */
    private String outTradeNo;

    /** 订单标题 */
    private String subject;

    /** 订单内容 */
    private String body;

    /** 用户备注 */
    private String remark;

    // ============= 地址信息

    /** 收货人手机号 */
    private String phoneNumber;

    /** 地址绑定的主键 */
    private Long addressBookId;

    /** 收货人姓名 */
    private String consigneeName;

    /** 详细地址 */
    private String detail;

    /**性别,0女,1男*/
    private Integer sex;

    /** 标签,0默认,1家,2学校,3公司,4其他*/
    private String label;

    //============ 用户信息

    /** 订单归属用户的主键 */
    private Long userId;

}
