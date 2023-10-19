package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.client.BaseClient;
import ru.practicum.ewm.dto.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static ru.practicum.ewm.constants.Constants.DATE_TIME_FORMATTER;

@Service
public class StatsClientService extends BaseClient {
    private static final String API_PREFIX = "/";

    @Autowired
    public StatsClientService(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> save(EndpointHit endpointHit) {
        return post("/hit", endpointHit);
    }

    public ResponseEntity<Object> findByStartAndEndAndUrisAndIsUniqueIp(LocalDateTime start,
                                                                        LocalDateTime end,
                                                                        List<String> uris,
                                                                        boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(DATE_TIME_FORMATTER),
                "end", end.format(DATE_TIME_FORMATTER),
                "uris", (uris == null || uris.isEmpty()) ? "" : String.join(",", uris),
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", null, parameters);
    }
}