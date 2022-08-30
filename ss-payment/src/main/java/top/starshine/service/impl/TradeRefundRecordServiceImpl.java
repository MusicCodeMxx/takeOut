package top.starshine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.starshine.commons.entity.paymenty.PaymentRecord;
import top.starshine.commons.entity.paymenty.TradeRefundRecord;
import top.starshine.mapper.PaymentRecordMapper;
import top.starshine.mapper.TradeRefundRecordMapper;
import top.starshine.service.PaymentRecordService;
import top.starshine.service.TradeRefundRecordService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * <h3>退款存根信息表服务接口实现类</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/8/3  下午 7:03  周三
 * @Description: 创作不容易, 记得关注点赞打赏一键三连
 */
@Service
public class TradeRefundRecordServiceImpl extends ServiceImpl<TradeRefundRecordMapper, TradeRefundRecord> implements TradeRefundRecordService {


    @Async // spring 提供的，通过 aop 切入的异步注解
    @Override
    public void saveSystemRefund(String body, Map<String, String> params) {
        try {
            if (!StringUtils.hasText(body)) throw new RuntimeException("数据体为空");
            if (CollectionUtils.isEmpty(params)) throw new RuntimeException("响应参数为空");

            TradeRefundRecord alipayTradeRefundDetails = new TradeRefundRecord();
            // 处理响应头的数据
            Map<String, String> content = new ObjectMapper().readValue(params.get("biz_content"), new TypeReference<Map<String, String>>() {});
            params.clear();
            alipayTradeRefundDetails.setOutTradeNo(content.get("out_trade_no"));
            alipayTradeRefundDetails.setTradeNo(content.get("trade_no"));
            String refund_amount = content.get("refund_amount");
            if (StringUtils.hasText(refund_amount))alipayTradeRefundDetails.setRefundAmount(Double.valueOf(refund_amount));
            alipayTradeRefundDetails.setRefundReason(content.get("refund_reason"));
            alipayTradeRefundDetails.setOutRequestNo(content.get("out_request_no"));
            content.clear();
            content = null;

            // 处理响应体里面的数据
            Map<String, Object> bodyMap = new ObjectMapper().readValue(body, new TypeReference<Map<String, Object>>() {});
            alipayTradeRefundDetails.setSign(bodyMap.get("sign").toString());
            // 处理嵌套数据
            bodyMap = (Map<String,Object>) bodyMap.get("alipay_trade_refund_response");
            alipayTradeRefundDetails.setCode(bodyMap.get("code").toString());
            alipayTradeRefundDetails.setMsg(bodyMap.get("msg").toString());
            alipayTradeRefundDetails.setBuyerLogonId(bodyMap.get("buyer_logon_id").toString());
            alipayTradeRefundDetails.setBuyerUserId(bodyMap.get("buyer_user_id").toString());
            alipayTradeRefundDetails.setFundChange(bodyMap.get("fund_change").toString());
            String gmt_refund_pay = bodyMap.get("gmt_refund_pay").toString();
            try { if (StringUtils.hasText(gmt_refund_pay)) alipayTradeRefundDetails.setGmtRefundPay(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(gmt_refund_pay)); }catch (ParseException ignored) {}
            String refund_fee = bodyMap.get("refund_fee").toString();
            if (StringUtils.hasText(refund_fee))alipayTradeRefundDetails.setRefundFee(Double.valueOf(refund_fee));
            String send_back_fee = bodyMap.get("send_back_fee").toString();
            if (StringUtils.hasText(send_back_fee))alipayTradeRefundDetails.setSendBackFee(Double.valueOf(send_back_fee));
            bodyMap.clear();
            // 保存到数据库
            super.save(alipayTradeRefundDetails);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
