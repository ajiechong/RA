package org.example.researchassistant.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
@Configuration
@PropertySource("classpath:application.properties")
public class OpenAiConfig {
    @Value("${openai.retry-count:5}")
    private Integer retryCount;

    @Value("${openai.initial-timeout:10000}")
    private Integer initialTimeout;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        //config message converter
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(
                MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_XML));
        restTemplate.setMessageConverters(Collections.singletonList(converter));

        //config request factory
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(initialTimeout);
        requestFactory.setReadTimeout(initialTimeout);
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;

    }

}
