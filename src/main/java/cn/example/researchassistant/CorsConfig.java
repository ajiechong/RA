package cn.example.researchassistant;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //是否发送Cookie
                .allowCredentials(true)
                //放行哪些原始域，也不能使用通识的，必须手动指定放行的
                .allowedOrigins("http://localhost:8080","http://localhost:8081","http://localhost:8082")
                .allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE"})
                //head也需要写明写明哪些头部信息
                .allowedHeaders("*")
                // 暴露哪些头部信息（具体列出需要暴露的头部）,不能直接使用通配符*
                .exposedHeaders("Authorization", "Cache-Control", "Content-Type");
    }
}
