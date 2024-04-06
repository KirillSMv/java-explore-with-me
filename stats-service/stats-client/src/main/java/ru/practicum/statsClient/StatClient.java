package ru.practicum.statsClient;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.statsDto.NewStatsDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatClient {

    private final RestTemplate restTemplate;

/*    @Autowired
    public StatClient(@Value("${stats-service.url}") String serverUrl, RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }*/

    public String postStats(NewStatsDto newStatsDto) {
        HttpEntity<NewStatsDto> requestEntity = new HttpEntity<>(newStatsDto, defaultHeaders());
        return restTemplate.postForObject("/hit", requestEntity, String.class);
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
