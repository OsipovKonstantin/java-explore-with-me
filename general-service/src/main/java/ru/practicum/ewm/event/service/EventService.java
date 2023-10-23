package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.entity.Event;
import ru.practicum.ewm.event.entity.EventState;
import ru.practicum.ewm.event.entity.SortType;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventService {
    List<EventShortDto> findByInitiatorId(Long userId, Integer from, Integer size);

    EventFullDto save(Long userId, NewEventDto newEventDto);

    EventFullDto findByIdAndInitiatorId(Long userId, Long eventId);

    Event findById(Long eventId);

    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> findParticipationRequestsByEventId(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateParticipationRequestsStatus(Long userId, Long eventId,
                                                                     EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    List<EventFullDto> findAdminByFilters(List<Long> users, List<EventState> states, List<Long> categories,
                                          LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto update(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> findPublicByFilters(String text, Set<Long> categories, Boolean paid,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                            SortType sort, Integer from, Integer size, HttpServletRequest request);

    EventFullDto findDtoById(Long eventId, HttpServletRequest request);

    List<Event> findByIdIn(List<Long> eventIds);
}
