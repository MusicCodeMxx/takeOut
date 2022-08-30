package top.starshine.commons.converter;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import top.starshine.commons.entity.coupon.Coupon;
import top.starshine.commons.entity.coupon.CouponBatchDetail;
import top.starshine.commons.entity.coupon.CouponVo;

import java.util.List;

/**
 * <h3>优惠券服务 Bean 转换器</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 3:04  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Mapper(componentModel = "spring")
public interface CouponConverter {

    @InheritConfiguration(name = "couponBatchDetailToCouponVo")
    List<CouponVo> couponBatchDetailsToCouponVos(List<CouponBatchDetail> source);

    @Mappings({@Mapping(target = "id", ignore = true),
                @Mapping(source = "id", target = "batchId")})
    CouponVo couponBatchDetailToCouponVo(CouponBatchDetail source);

    @InheritConfiguration(name = "couponToCouponVo")
    List<CouponVo> couponsToCouponVos(List<Coupon> source);

    CouponVo couponToCouponVo(Coupon source);

}
