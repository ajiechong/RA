package org.example.researchassistant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
//@ComponentScan("cn.mentalhealth"),都在同一个包下面，就没有必要添加
public class SoWellLifeApplication {
    public static void main(String[] args) {
//        SpringApplication.run(SoWellLifeApplication.class, args);
        System.setProperty("spring.config.location", "classpath:/application.properties");
        ConfigurableApplicationContext context = SpringApplication.run(SoWellLifeApplication.class, args);

        // 验证配置加载
        ConfigurableEnvironment env = context.getEnvironment();
        Logger log = LoggerFactory.getLogger(SoWellLifeApplication.class);

        String apiKey = env.getProperty("openai.api-key");
        if (apiKey != null && !apiKey.isEmpty()) {
            log.info("Successfully loaded OpenAI API key from application.properties");
        } else {
            log.error("Failed to load OpenAI API key from application.properties");
        }
    }
}
