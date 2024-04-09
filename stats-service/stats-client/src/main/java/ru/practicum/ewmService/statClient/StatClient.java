package ru.practicum.ewmService.statClient;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.statsDto.NewStatsDto;
import ru.practicum.statsDto.StatsParamsDto;
import ru.practicum.statsDto.StatsToUserDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatClient {

    private final RestTemplate restTemplate;
    private final String serverUrl = "http://stats-service:9090";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public StatsToUserDto postStats(NewStatsDto newStatsDto) {
        HttpEntity<NewStatsDto> requestEntity = new HttpEntity<>(newStatsDto, defaultHeaders());
        return restTemplate.postForObject(serverUrl + "/hit", requestEntity, StatsToUserDto.class);
    }

    public List<StatsToUserDto> getStats(StatsParamsDto statsParamsDto) {
        String url;
        Map<String, Object> params;
        if (statsParamsDto.getUris() == null) {
            url = serverUrl + "/stats?start={start}&end={end}&unique={unique}";
            params = Map.of(
                    "start", getEncodedAndFormattedTime(statsParamsDto.getStart()),
                    "end", getEncodedAndFormattedTime(statsParamsDto.getEnd()),
                    "unique", statsParamsDto.isUnique());
        } else {
            url = serverUrl + "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
            params = Map.of(
                    "start", getEncodedAndFormattedTime(statsParamsDto.getStart()),
                    "end", getEncodedAndFormattedTime(statsParamsDto.getEnd()),
                    "uris", getUrisAsString(statsParamsDto.getUris()),
                    "unique", statsParamsDto.isUnique());
        }
        StatsToUserDto[] result = restTemplate.getForObject(url, StatsToUserDto[].class, params);
        return Arrays.asList(result);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private String getUrisAsString(List<String> uris) {
        return String.join(",", uris);
    }

    private String getEncodedAndFormattedTime(LocalDateTime time) {
        return URLEncoder.encode(time.format(formatter), StandardCharsets.UTF_8);
    }
}
