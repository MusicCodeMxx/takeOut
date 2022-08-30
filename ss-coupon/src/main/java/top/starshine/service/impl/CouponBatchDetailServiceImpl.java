package top.starshine.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.starshine.commons.entity.coupon.CouponBatchDetail;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.exception.BadRequestException;
import top.starshine.commons.handle.ThreadLocalCache;
import top.starshine.commons.status.CouponStatus;
import top.starshine.mapper.CouponBatchDetailMapper;
import top.starshine.service.CouponBatchDetailService;
import top.starshine.service.CouponService;

import java.util.Date;
import java.util.List;

/**
 * <h3>优惠券批次服务实现类</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/1  下午 2:56  周一
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Service
@RequiredArgsConstructor
public class CouponBatchDetailServiceImpl extends ServiceImpl<CouponBatchDetailMapper,CouponBatchDetail> implements CouponBatchDetailService {

    private final CouponService couponService;

    @Override
    @Transactional
    public Long receiveCouponById(Long id) {
        User user = ThreadLocalCache.getNotNull();

        // 获取指定获取
        Date date = new Date();
        CouponBatchDetail couponBatchDetail = new LambdaQueryChainWrapper<>(getBaseMapper())
                .eq(CouponBatchDetail::getId, id)
                .eq(CouponBatchDetail::getIsDelete, 0)
                .le(CouponBatchDetail::getStartTime, date)
                .ge(CouponBatchDetail::getEndTime, date)
                .one();

        // 检查是否符合领取规则
        if (couponBatchDetail.getTotalCount()==0) throw new BadRequestException(CouponStatus.COUPON_DOES_NOT_EXIST);
        if (couponBatchDetail.getReceiveCount() >= couponBatchDetail.getTotalCount()) throw new BadRequestException(CouponStatus.NO_COUPON);

        // 为什么要这样做, 这里主要时检查然后反馈给前端状态
        switch (couponBatchDetail.getStatus()){
            case 0: throw new BadRequestException(CouponStatus.COUPON_RECEIVE_NO_START);// 0未发布
            case 1: break;
            case 2: throw new BadRequestException(CouponStatus.NO_COUPON);// 2领取完
            case 3: throw new BadRequestException(CouponStatus.EXPIRED);// 3过期
            case 4: throw new BadRequestException(CouponStatus.FREEZE_COUPON);// 4冻结
            case 5: throw new BadRequestException(CouponStatus.COUPON_ERROR);// 5异常
        }

        // 增加领取量
        new LambdaUpdateChainWrapper<>(getBaseMapper())
                .set(CouponBatchDetail::getReceiveCount,couponBatchDetail.getReceiveCount() + 1)
                .eq(CouponBatchDetail::getId,id)
                .update();

        // 写入领取信息,添加券
        return couponService.addCoupon(couponBatchDetail);
    }

    @Override
    public List<CouponBatchDetail> findList() {
        // 开始时间大于等于当前时间，获取当前公开，未过期的，可以领取
        return getBaseMapper().findList();
    }

}
