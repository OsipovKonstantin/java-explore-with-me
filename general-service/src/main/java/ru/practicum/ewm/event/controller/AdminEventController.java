package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventState;
import ru.practicum.ewm.event.dto.FindByAdminFiltersParams;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.constants.Constants.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<EventFullDto> findByAdminFilters(@RequestParam(required = false) List<Long> users,
                                                 @RequestParam(required = false) List<EventState> states,
                                                 @RequestParam(required = false) List<Long> categories,
                                                 @DateTimeFormat(pattern = DATE_TIME_PATTERN) @RequestParam(required = false)
                                                 LocalDateTime rangeStart,
                                                 @DateTimeFormat(pattern = DATE_TIME_PATTERN) @RequestParam(required = false)
                                                 LocalDateTime rangeEnd,
                                                 @Min(MIN_PAGE_FROM) @RequestParam(defaultValue = "0") Integer from,
                                                 @Min(MIN_PAGE_SIZE) @RequestParam(defaultValue = "10")
                                                 Integer size) {
        return eventService.findByAdminFilters(
                new FindByAdminFiltersParams(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@PathVariable Long eventId,
                               @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return eventService.update(eventId, updateEventAdminRequest);
    }
}
