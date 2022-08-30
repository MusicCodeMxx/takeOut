package top.starshine;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import top.starshine.commons.aspect.EnableMyThreadPool;

/**
 * <h3>订单微服务启动类</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/25  上午 12:10  周一
 * @Description: hello world
 */
@EnableAsync // 异步开启
@EnableMyThreadPool // 开启自定义异步线程池
@EnableDubbo // 开 dubbo 服务
@EnableDiscoveryClient  //开启服务注册功能配置功能
@SpringBootApplication
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
        System.out.println("|========================| START OK! |========================|");
    }

}
