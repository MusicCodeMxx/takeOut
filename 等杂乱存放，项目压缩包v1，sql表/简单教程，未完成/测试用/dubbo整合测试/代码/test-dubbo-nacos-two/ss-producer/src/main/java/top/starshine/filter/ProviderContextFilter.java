package top.starshine.filter;

import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;


@Activate(group = "PROVIDER")
public class ProviderContextFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {

            System.out.println(invocation.getAttachment("token"));

        }catch(Exception ignored) {}

        System.out.println("生产者拦截器");

        return invoker.invoke(invocation);//执行业务逻辑
    }

}