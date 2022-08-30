package top.starshine.commons.model.web.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * <h3></h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/27  下午 6:39  周三
 * @Description: hello world
 */
@Configuration
@RequiredArgsConstructor
public class HttpClientService {

    private final RestTemplate restTemplate;

    public Map<String,String> getIpAddress(String ip){
        if (!StringUtils.hasText(ip)) return null;
        String url = "http://whois.pconline.com.cn/ipJson.jsp?ip=" + ip + "&json=true";
        try {
            String body = restTemplate.getForObject(url, String.class);
            if (!StringUtils.hasText(body)) return null;
            return new ObjectMapper().readValue(body, new TypeReference<Map<String, String>>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
