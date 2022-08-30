package top.starshine.commons.entity.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <h3>订单视图对象</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/4  上午 12:01  周四
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Setter
@Getter
public class OrderDetailVo implements java.io.Serializable{

    /** 主键,自动注入 */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    // ============== 订单流程状态

    /**订单状态,1待付款,2后厨制作中,3制作完成待派送,4正在派送中,5已派送完成,6已完成,7已取消,8超时未支付已自动取消,9退款申请,10退款成功 */
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

    /**支付宝交易号，支付宝交易凭证号*/
    private String tradeNo;

    /** 用户备注 */
    private String remark;

    // ============= 地址信息

    /** 收货人手机号 */
    private String phoneNumber;

    /** 收货人姓名 */
    private String consigneeName;

    /** 详细地址 */
    private String detail;

    /**性别,0女,1男*/
    private Integer sex;

    /** 标签,0默认,1家,2学校,3公司,4其他*/
    private String label;

    /**操作记录的时间*/
    private Map<String, Date> operationTime;

    /**子订单列表(产品)*/
    List<ChildOrderDetailVo> childOrderDetailVos;

}
