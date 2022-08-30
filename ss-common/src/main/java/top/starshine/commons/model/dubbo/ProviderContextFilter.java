package top.starshine.commons.model.dubbo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.util.StringUtils;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.handle.ThreadLocalCache;
import top.starshine.commons.util.AesUtils;

/**
 * <h3>生产者接收到请求过滤器</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/25  上午 12:00  周一
 * @Description: hello world
 */
@Slf4j
@Activate(group = "PROVIDER")
public class ProviderContextFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            // 获取请求中的用户信息
            String userAesData = invocation.getAttachment(DubboContextConstant.USER_DETAIL_KEY);
            // 检查是否有携带
            if (null!=userAesData){
                // 解密数据
                String userJson = AesUtils.decryptFromBase64(userAesData, DubboContextConstant.USER_AES_KEY);
                userAesData = null;
                if (StringUtils.hasText(userJson)){
                    // json 转对象
                    User user = new ObjectMapper().readValue(userJson, User.class);
                    // 存入线程
                    if (null!=user) ThreadLocalCache.put(user);
                    userJson = null;
                }
            }
        }catch(Exception ignored) {}

        log.info(">>>>>>>>>>>>>>>>>>>> ProviderContextFilter run <<<<<<<<<<<<<<<<<<<<<");

        try {
            return invoker.invoke(invocation);//执行业务逻辑
        } finally {
            ThreadLocalCache.remove();// 移除本地线程缓存
        }
    }

}