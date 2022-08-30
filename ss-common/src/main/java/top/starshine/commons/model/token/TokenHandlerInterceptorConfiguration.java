package top.starshine.commons.model.token;

/**
 * <h3>令牌拦截器注册配置类</h3>
 *
 * @author: starshine
 * @email: 183101655@qq.com
 * @version: 1.0
 * @since: 2022/7/24  下午 11:20  周日
 * @Description: hello world
 */
//@EnableWebMvc
//@Configuration
//@RequiredArgsConstructor
//public class TokenHandlerInterceptorConfiguration implements WebMvcConfigurer {
//
//    /** 令牌拦截器 */
//    private final TokenInterceptor tokenInterceptor;
//
//    /**
//     * 注册拦截器
//     * @param registry 拦截器注册表
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//
//        /* 令牌拦截器优先,令牌认证再到安全认证 */
//        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**").order(-1);// 所有请求都要经过令牌拦截检查
//
//    }
//
//}
