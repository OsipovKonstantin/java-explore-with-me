package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.service.StatsClientService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.constants.Constants.DATE_TIME_PATTERN;

@Validated
@RestController
@RequiredArgsConstructor
public class StatsClientController {
    private final StatsClientService statsClientService;

    @PostMapping("/hit")
    public ResponseEntity<Object> save(@Valid @RequestBody EndpointHit endpointHit) {
        return statsClientService.save(endpointHit);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> findByStartAndEndAndUrisAndIsUniqueIp(
            @RequestParam @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique) {
        return statsClientService.findByStartAndEndAndUrisAndIsUniqueIp(start, end, uris, unique);
    }
}
