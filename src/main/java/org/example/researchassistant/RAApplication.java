package org.example.researchassistant;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
@MapperScan("org.example.researchassistant.mapper")
public class RAApplication {
    public static void main(String[] args) {
//        SpringApplication.run(SoWellLifeApplication.class, args);
        System.setProperty("spring.config.location", "classpath:/application.properties");
        ConfigurableApplicationContext context = SpringApplication.run(RAApplication.class, args);

        // 验证配置加载
        ConfigurableEnvironment env = context.getEnvironment();
        Logger log = LoggerFactory.getLogger(RAApplication.class);

        String apiKey = env.getProperty("openai.api-key");
        if (apiKey != null && !apiKey.isEmpty()) {
            log.info("Successfully loaded OpenAI API key from application.properties");
        } else {
            log.error("Failed to load OpenAI API key from application.properties");
        }
    }
}
