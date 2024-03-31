package ru.practicum.statsClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.statsDto.GetStatsParamsDto;
import ru.practicum.statsDto.NewStatsDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatsClient(@Value("${stats-service.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> postStats(NewStatsDto body) {
        return post(body.getUri(), body);
    }

    protected ResponseEntity<Object> getStats(GetStatsParamsDto getStatsParamsDto) {
        Map<String, Object> params = Map.of(
                "start", URLEncoder.encode(getStatsParamsDto.getStart().format(formatter), StandardCharsets.UTF_8),
                "end", URLEncoder.encode(getStatsParamsDto.getEnd().format(formatter), StandardCharsets.UTF_8),
                "uris", getStatsParamsDto.getUris(),
                "unique", getStatsParamsDto.getUnique());
        return get("/stats", params);
    }
}
