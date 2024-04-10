package ru.practicum.ewmService.statClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class StatClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

