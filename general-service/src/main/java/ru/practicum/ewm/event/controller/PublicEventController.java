package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.entity.SortType;
import ru.practicum.ewm.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static ru.practicum.ewm.constants.Constants.DATE_TIME_PATTERN;

@Validated
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> findPublicByFilters(@RequestParam(required = false) String text,
                                                   @RequestParam(required = false) Set<Long> categories,
                                                   @RequestParam(required = false) Boolean paid,
                                                   @RequestParam(required = false)
                                                   @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeStart,
                                                   @RequestParam(required = false)
                                                   @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeEnd,
                                                   @RequestParam(required = false) Boolean onlyAvailable,
                                                   @RequestParam(required = false) SortType sort,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size,
                                                   HttpServletRequest request) {
        return eventService.findPublicByFilters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size,
                request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto findById(@PathVariable Long id,
                                 HttpServletRequest request) {
        return eventService.findDtoById(id, request);
    }
}
