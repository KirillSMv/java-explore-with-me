package ru.practicum.ewmService.statClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.statsDto.NewStatsDto;
import ru.practicum.statsDto.StatsToUserDto;

import java.util.List;

@Service
public class StatClient {

    private final RestTemplate restTemplate;

    @Autowired
    public StatClient(@Value("${stats-service.url}") String serverUrl, RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public StatsToUserDto postStats(NewStatsDto newStatsDto) {
        HttpEntity<NewStatsDto> requestEntity = new HttpEntity<>(newStatsDto, defaultHeaders());
        return restTemplate.postForObject("/hit", requestEntity, StatsToUserDto.class);
    }

/*
    public List<StatsDtoToUser> getStats(NewStatsDto newStatsDto) {
        StatsDtoToUserList response = restTemplate.getForObject("uri", StatsDtoToUserList.class);
        return response.getStatsToUserDtoList();
    }
*/

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }


}
