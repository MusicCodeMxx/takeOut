package top.starshine.commons.model.dubbo;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.handle.ThreadLocalCache;
import top.starshine.commons.util.AesUtils;

/**
 * <h3>消费者发起请求过滤器</h3>
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/25  上午 12:00  周一
 * @Description: hello world
 */
@Slf4j
@Activate(group = "CONSUMER")
public class ConsumerContextFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            // 获取当前线程的用户
            User user = ThreadLocalCache.get();
            // 检查是否有 > 加密 > 存入
            if (null != user) invocation.setAttachment(DubboContextConstant.USER_DETAIL_KEY,
                    AesUtils.encryptObjectMapperToBase64(user, DubboContextConstant.USER_AES_KEY));
        }catch(Exception ignored) {}

        log.info(">>>>>>>>>>>>>>>>>>>> ConsumerContextFilter run <<<<<<<<<<<<<<<<<<<<<");

        try {
            return invoker.invoke(invocation);//执行业务逻辑
        } finally {
            RpcContext.getContext().clearAttachments();//清理
        }
    }

}