package top.starshine.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.starshine.commons.entity.coupon.Coupon;
import top.starshine.commons.entity.coupon.CouponBatchDetail;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.exception.BadRequestException;
import top.starshine.commons.exception.InternalServerErrorException;
import top.starshine.commons.handle.ThreadLocalCache;
import top.starshine.commons.status.CouponStatus;
import top.starshine.mapper.CouponMapper;
import top.starshine.service.CouponService;

import java.util.Date;
import java.util.List;

/**
 * <h3>优惠券服务接口</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 2:52  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Service
@RequiredArgsConstructor
public class CouponServiceImpl extends ServiceImpl<CouponMapper,Coupon> implements CouponService {

    @Override
    public List<Coupon> meCouponList() {
        User user = ThreadLocalCache.getNotNull();
        Date date = new Date();
        return new LambdaQueryChainWrapper<>(getBaseMapper())
                .eq(Coupon::getUserId, user.getId())
                .eq(Coupon::getStatus,0)
                .eq(Coupon::getIsDelete, 0)
                .le(Coupon::getStartTime, date)
                .ge(Coupon::getEndTime, date)
                .select(Coupon::getId,
                        Coupon::getBatchId,
                        Coupon::getCouponName,
                        Coupon::getDescription,
                        Coupon::getThreshold,
                        Coupon::getPrice,
                        Coupon::getStartTime,
                        Coupon::getEndTime)
                .list();
    }

    @Override
    @Transactional
    public Long addCoupon(CouponBatchDetail couponBatchDetail) {
        User user = ThreadLocalCache.getNotNull();

        // 是否已领取
        Integer count = new LambdaQueryChainWrapper<>(getBaseMapper())
                .eq(Coupon::getBatchId, couponBatchDetail.getId())
                .eq(Coupon::getUserId, user.getId())
                .eq(Coupon::getIsDelete, 0)
                .eq(Coupon::getStatus, 0)
                .count();
        if (count > 0) throw new BadRequestException(CouponStatus.YOU_RECEIVE_COUPON);

        // 创建领取信息
        Coupon coupon = new Coupon()
                .setStatus(0)
                .setUserId(user.getId())
                .setBatchId(couponBatchDetail.getId())
                .setPrice(couponBatchDetail.getPrice())
                .setEndTime(couponBatchDetail.getEndTime())
                .setThreshold(couponBatchDetail.getThreshold())
                .setStartTime(couponBatchDetail.getStartTime())
                .setCouponName(couponBatchDetail.getCouponName())
                .setDescription(couponBatchDetail.getDescription());
        coupon.setIsDelete(0)
                .setCreateById(user.getId())
                .setUpdateById(user.getId());

        // 添加用户到数据库
        boolean flag = save(coupon);
        if (!flag) throw new InternalServerErrorException(CouponStatus.RECEIVE_COUPON_ERROR);
        return coupon.getId();
    }

    @Override
    public List<Coupon> selectIdAndBatchId() {
        User user = ThreadLocalCache.getNotNull();
        // 开始时间大于等于当前时间，获取当前公开，未过期的，可以领取
        Date date = new Date();
        return new LambdaQueryChainWrapper<>(getBaseMapper())
                .eq(Coupon::getUserId, user.getId())
                .eq(Coupon::getIsDelete, 0)
                .eq(Coupon::getStatus, 0)
                .le(Coupon::getStartTime, date)
                .ge(Coupon::getEndTime, date)
                .select(Coupon::getId, Coupon::getBatchId)
                .list();
    }

}
