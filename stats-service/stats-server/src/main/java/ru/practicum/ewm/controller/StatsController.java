package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.constants.Constants.DATE_TIME_PATTERN;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody EndpointHit endpointHit) {
        statsService.save(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> findByStartAndEndAndUrisAndIsUniqueIp(
            @RequestParam @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique) {
        return statsService.findByStartAndEndAndUrisAndIsUniqueIp(start, end, uris, unique);
    }

    @GetMapping("/stats/event-views/{id}")
    public Long countByUri(@PathVariable Long id) {
        return statsService.countByUri("/events/" + id);
    }
}
