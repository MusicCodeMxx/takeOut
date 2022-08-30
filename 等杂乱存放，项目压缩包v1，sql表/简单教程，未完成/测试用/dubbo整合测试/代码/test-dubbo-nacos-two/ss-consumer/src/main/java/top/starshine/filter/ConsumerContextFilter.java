package top.starshine.filter;

import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

@Activate(group = "CONSUMER")
public class ConsumerContextFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {

            invocation.setAttachment("token","123");

        }catch(Exception e) {
            e.printStackTrace();
        }

        System.out.println("消费者拦截器");

        try {
            return invoker.invoke(invocation);//执行业务逻辑
        } finally {
            RpcContext.getContext().clearAttachments();//清理
        }

    }

}