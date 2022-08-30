package top.starshine.commons.util;

import eu.bitwalker.useragentutils.BrowserType;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * <h3> Request User Agent Utils </h3>
 * <h3> 请求体 用户 客户端 工具类 </h3>
 * @author: starshine
 * @version: 1.0
 * @since: 2022/7/19  下午 2:45  周二
 * @Description:
 */
public final class UserAgentUtils {

    private static final String USER_AGENT = "User-Agent";

    /**
     * <h3>获取当前客户端请求的终端类型</h3>
     * @return {@link BrowserType} 终端类型枚举
     */
    public static BrowserType getBrowserType(){
        return UserAgent.parseUserAgentString(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest()
                        .getHeader(USER_AGENT))
                        .getBrowser()
                        .getBrowserType();
    }


    public static UserAgent getUserAgent(HttpServletRequest request){
        return UserAgent.parseUserAgentString(request.getHeader(USER_AGENT));
    }


}
