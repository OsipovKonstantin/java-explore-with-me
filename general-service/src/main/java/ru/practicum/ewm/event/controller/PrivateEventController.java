package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> findByInitiatorId(@PathVariable Long userId,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        return eventService.findByInitiatorId(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto save(@PathVariable Long userId,
                             @Valid @RequestBody NewEventDto newEventDto) {
        return eventService.save(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto findByIdAndInitiatorId(@PathVariable Long userId,
                                               @PathVariable Long eventId) {
        return eventService.findByIdAndInitiatorId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return eventService.update(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> findParticipationRequestsByEventId(@PathVariable Long userId,
                                                                            @PathVariable Long eventId) {
        return eventService.findParticipationRequestsByEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateParticipationRequestsStatus(@PathVariable Long userId,
                                                                            @PathVariable Long eventId,
                                                                            @Valid @RequestBody EventRequestStatusUpdateRequest
                                                                                    eventRequestStatusUpdateRequest) {
        return eventService.updateParticipationRequestsStatus(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
