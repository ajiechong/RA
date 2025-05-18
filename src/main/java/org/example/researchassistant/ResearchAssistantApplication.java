package org.example.researchassistant;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.researchassistant.mapper")
public class ResearchAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResearchAssistantApplication.class, args);
    }

}
