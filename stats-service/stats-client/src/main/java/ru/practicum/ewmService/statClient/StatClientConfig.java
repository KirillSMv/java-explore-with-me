package ru.practicum.ewmService.statClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class StatClientConfig {

    @Bean
    public RestTemplate restTemplate() {

        /*
        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);
         */
        return new RestTemplate();
    }
}

