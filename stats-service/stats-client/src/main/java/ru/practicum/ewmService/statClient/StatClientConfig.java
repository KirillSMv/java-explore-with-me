package ru.practicum.ewmService.statClient;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;


@Configuration
public class StatClientConfig {

    private final String serverUrl = "http://stats-service:9090";

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .build();
    }
}

