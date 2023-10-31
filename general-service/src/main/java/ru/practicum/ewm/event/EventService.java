package ru.practicum.ewm.event;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.entity.Event;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    List<EventShortDto> findByInitiatorId(Long userId, Integer from, Integer size);

    EventFullDto save(Long userId, NewEventDto newEventDto);

    EventFullDto findByIdAndInitiatorId(Long userId, Long eventId);

    Event findById(Long eventId);

    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> findParticipationRequestsByEventId(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateParticipationRequestsStatus(Long userId, Long eventId,
                                                                     EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    List<EventFullDto> findByAdminFilters(FindByAdminFiltersParams params);

    EventFullDto update(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> findByPublicFilters(FindByPublicFiltersParams params);

    EventFullDto findDtoById(Long eventId, HttpServletRequest request);

    List<Event> findByIdIn(List<Long> eventIds);

    Event findByIdAndState(Long eventId, EventState state);
}
