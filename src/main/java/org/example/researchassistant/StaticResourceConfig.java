package org.example.researchassistant;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射 /uploads/** 到 D:/SoWell-Life/SoWellLife_v2/src/front_end/src/assets/imgs/avatar/
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:D:/ilikestudyhhhmaybe/Project/MentalHealth/SoWellLife_v2/src/front_end/src/assets/imgs/avatar");
    }
}
