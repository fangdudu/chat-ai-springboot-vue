package life.chat_ai.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyGlobalCorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 对所有的路径允许跨域请求
        registry.addMapping("/**")
                // 允许来自任何源的请求
                .allowedOrigins("*")
                // 允许的请求方法
                .allowedMethods("GET", "POST")
                // 允许的请求头
                .allowedHeaders("*")
                // 是否允许证书（cookies），根据需要设置
                .allowCredentials(false)
                // 预检请求的缓存时间（秒）
                .maxAge(3600);
    }
}