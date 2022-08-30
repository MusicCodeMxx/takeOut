package top.starshine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.starshine.commons.entity.login.LoginRecord;
import top.starshine.commons.model.web.http.HttpClientService;
import top.starshine.commons.util.CollectionUtils;
import top.starshine.mapper.LoginRecordMapper;
import top.starshine.service.LoginRecordService;

import java.util.Map;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/27  下午 5:52  周三
 * @Description: hello world
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginRecordServiceImpl extends ServiceImpl<LoginRecordMapper, LoginRecord> implements LoginRecordService {

    private final HttpClientService httpClientService;

    @Async
    @Override
    public void saveRecord(UserAgent userAgent, String ip, Long userId, String type) {
        LoginRecord loginRecord = new LoginRecord();
        try {
            loginRecord.setUserId(userId);
            loginRecord.setIp(ip);
            // 如果存入 -1 就是退出登录
            loginRecord.setType(type);

            Browser browser = userAgent.getBrowser();
            if ( null != browser){
                loginRecord.setBrowserName(browser.getName());
                loginRecord.setManufacturer(browser.getManufacturer().getName());
            }

            Version browserVersion = userAgent.getBrowserVersion();
            if (null != browserVersion) loginRecord.setBrowserVersion(browserVersion.getVersion());

            OperatingSystem operatingSystem = userAgent.getOperatingSystem();
            loginRecord.setSystemName(operatingSystem.getName());
            loginRecord.setDeviceType(operatingSystem.getDeviceType().getName());
            loginRecord.setSystemGroup(operatingSystem.getGroup().getName());

            // 获取 IP 地址
            log.info("登录IP=>{}",ip);
            Map<String, String> addressDetail = httpClientService.getIpAddress(ip);
            if (CollectionUtils.isNotEmpty(addressDetail)){
                loginRecord.setAddress(addressDetail.get("addr"));
                loginRecord.setCityCode(addressDetail.get("cityCode"));
                loginRecord.setProCode(addressDetail.get("proCode"));
                addressDetail = null;
            }

            super.save(loginRecord);
            log.info("保存完毕");
        } finally {
            loginRecord = null;
        }
    }

}
