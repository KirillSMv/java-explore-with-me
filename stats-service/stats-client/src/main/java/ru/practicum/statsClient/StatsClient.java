package ru.practicum.statsClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import ru.practicum.statsDto.NewStatsDto;
import ru.practicum.statsDto.StatsParamsDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatsClient(RestTemplateBuilder builder) { //@Value("${stats-service.url}") String serverUrl,
        super(
                builder
                        //.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> postStats(NewStatsDto body) {
        return post(body.getUri(), body);
    }

    protected ResponseEntity<Object> getStats(StatsParamsDto statsParamsDto) {
        Map<String, Object> params = Map.of(
                "start", getEncodedAndFormattedTime(statsParamsDto.getStart()),
                "end", getEncodedAndFormattedTime(statsParamsDto.getEnd()),
                "uris", getUrisAsString(statsParamsDto.getUris()),
                "unique", statsParamsDto.isUnique());
        return get("/stats", params);
    }

    private String getUrisAsString(List<String> uris) {
        return String.join(",", uris);
    }

    private String getEncodedAndFormattedTime(LocalDateTime time) {
        return URLEncoder.encode(time.format(formatter), StandardCharsets.UTF_8);
    }
}
