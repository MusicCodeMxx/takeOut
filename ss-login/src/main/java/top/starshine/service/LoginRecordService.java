package top.starshine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import eu.bitwalker.useragentutils.UserAgent;
import top.starshine.commons.entity.login.LoginRecord;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/27  下午 5:52  周三
 * @Description: hello world
 */
public interface LoginRecordService extends IService<LoginRecord> {

    /**
     * 保存记录
     * @param userAgent 用户代理
     * @param ip ip地址
     * @param userId 用户主键
     * @param type 类型枚举
     */
    void saveRecord(UserAgent userAgent, String ip, Long userId, String type);

}
