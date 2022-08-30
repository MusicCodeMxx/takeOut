package top.starshine.commons.model.web.http;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


/**
 * <h3>Http 请求模板配置类</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/27  下午 6:11  周三
 * @Description: hello world
 */
@Slf4j
@Configuration
public class RestTemplateConfigure {

    /**
     * 请求连接池配置
     * @param httpClient http 客户端
     * @return 客户端 http 请求工厂
     */
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(OkHttpClient httpClient) {
        return new OkHttp3ClientHttpRequestFactory(httpClient);
    }

    /**
     * rest模板
     * @return RestTemplate 模板对象
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        // boot 中可使用 RestTemplateBuilder.build 创建 // 配置请求工厂
        //RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        // 消息转换器中添加  Jackson2 序列化
        //restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        // 可以增加拦截器
        //restTemplate.setInterceptors(...);
        //return restTemplate;
        return new RestTemplate(clientHttpRequestFactory);
    }


}
