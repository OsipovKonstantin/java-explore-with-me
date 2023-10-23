package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.entity.Category;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.entity.*;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.mapper.LocationMapper;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.*;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.entity.ParticipationRequest;
import ru.practicum.ewm.request.entity.RequestStatus;
import ru.practicum.ewm.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.request.service.ParticipationRequestService;
import ru.practicum.ewm.user.entity.User;
import ru.practicum.ewm.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final ParticipationRequestService requestService;
    private final StatsClient statsClient;
    private final UserService userService;
    private final CategoryService categoryService;
    private final LocationService locationService;

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> findByInitiatorId(Long userId, Integer from, Integer size) {
        Pageable page = new OffsetBasedPageRequest(from, size);
        userService.findById(userId);

        return eventRepository.findByInitiatorId(userId, page).stream().map(e ->
                        EventMapper.toEventShortDto(e,
                                requestService.countByEventIdAndStatus(e.getId(), RequestStatus.CONFIRMED),
                                statsClient.countByEventId(e.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto save(Long userId, NewEventDto newEventDto) {
        User initiator = userService.findById(userId);
        Category category = categoryService.findById(newEventDto.getCategory());
        Location location = locationService.save(newEventDto.getLocation());

        return EventMapper.toEventFullDto(eventRepository.save(EventMapper.toEvent(newEventDto, initiator, category,
                        location)),
                0L, 0L);
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto findByIdAndInitiatorId(Long userId, Long eventId) {
        Event event = checkEventAndUser(userId, eventId);

        return EventMapper.toEventFullDto(event,
                requestService.countByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED),
                statsClient.countByEventId(event.getId()));
    }

    @Transactional(readOnly = true)
    @Override
    public Event findById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Событие с id %d не найдено.", eventId)));
    }

    @Override
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = checkEventAndUser(userId, eventId);
        if (!event.getState().equals(EventState.CANCELED) && !event.getState().equals(EventState.PENDING))
            throw new InvalidEventStateException("Статус события должен быть отмененным или в ожидании.");

        EventState newState;
        if (updateEventUserRequest.getStateAction() == null)
            newState = event.getState();
        else if (updateEventUserRequest.getStateAction().equals(UserEventStateAction.SEND_TO_REVIEW))
            newState = EventState.PENDING;
        else
            newState = EventState.CANCELED;

        return EventMapper.toEventFullDto(eventRepository.save(event
                        .setAnnotation(updateEventUserRequest.getAnnotation() == null
                                ? event.getAnnotation()
                                : updateEventUserRequest.getAnnotation())
                        .setCategory(updateEventUserRequest.getCategory() == null
                                ? event.getCategory()
                                : categoryService.findById(updateEventUserRequest.getCategory()))
                        .setDescription(updateEventUserRequest.getDescription() == null
                                ? event.getDescription()
                                : updateEventUserRequest.getDescription())
                        .setEventDate(updateEventUserRequest.getEventDate() == null
                                ? event.getEventDate()
                                : updateEventUserRequest.getEventDate())
                        .setLocation(updateEventUserRequest.getLocation() == null
                                ? event.getLocation()
                                : LocationMapper.toLocation(updateEventUserRequest.getLocation()))
                        .setPaid(updateEventUserRequest.getPaid() == null
                                ? event.getPaid()
                                : updateEventUserRequest.getPaid())
                        .setParticipantLimit(updateEventUserRequest.getParticipantLimit() == null
                                ? event.getParticipantLimit()
                                : updateEventUserRequest.getParticipantLimit())
                        .setRequestModeration(updateEventUserRequest.getRequestModeration() == null
                                ? event.getRequestModeration()
                                : updateEventUserRequest.getRequestModeration())
                        .setState(newState)
                        .setTitle(updateEventUserRequest.getTitle() == null
                                ? event.getTitle()
                                : updateEventUserRequest.getTitle())),
                requestService.countByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED),
                statsClient.countByEventId(event.getId()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> findParticipationRequestsByEventId(Long userId, Long eventId) {
        checkEventAndUser(userId, eventId);
        return requestService.findByEventId(eventId);
    }

    @Override
    public EventRequestStatusUpdateResult updateParticipationRequestsStatus(Long userId, Long eventId,
                                                                            EventRequestStatusUpdateRequest
                                                                                    eventRequestStatusUpdateRequest) {
        Event event = checkEventAndUser(userId, eventId);
        if (event.getParticipantLimit() == 0 || Boolean.FALSE.equals(event.getRequestModeration()))
            throw new NoConfirmationNeeded("Подтверждение заявки на участие в событии не требуется " +
                    "при отсутствии лимита заявок или выключенной пре-модерации.");

        List<ParticipationRequest> requestsForUpdate = requestService
                .findByIdIn(eventRequestStatusUpdateRequest.getRequestIds());
        if (requestsForUpdate.stream().anyMatch(pr -> pr.getStatus() != RequestStatus.PENDING))
            throw new InvalidRequestStatusException("Все обновляемые заявки на участие в событии должны иметь статус " +
                    "в ожидании.");

        List<ParticipationRequest> newConfirmedRequests = new ArrayList<>();
        List<ParticipationRequest> newRejectedRequests = new ArrayList<>();

        if (eventRequestStatusUpdateRequest.getStatus().equals(RequestStatus.REJECTED)) {
            newRejectedRequests.addAll(requestService.saveAll(requestsForUpdate.stream()
                    .peek(pr -> pr.setStatus(RequestStatus.REJECTED)).collect(Collectors.toList())));
            return new EventRequestStatusUpdateResult()
                    .setConfirmedRequests(List.of())
                    .setRejectedRequests(newRejectedRequests.stream()
                            .map(ParticipationRequestMapper::toParticipationRequestDto).collect(Collectors.toList()));
        }

        Long confirmedRequests = requestService.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
        Integer participantLimit = event.getParticipantLimit();
        Long availableLimit = participantLimit - confirmedRequests;
        if (availableLimit <= 0)
            throw new ParticipantLimitAlreadyReached("Достигнут лимит заявок.");

        if (availableLimit >= requestsForUpdate.size())
            newConfirmedRequests.addAll(requestService.saveAll(requestsForUpdate.stream()
                    .peek(pr -> pr.setStatus((RequestStatus.CONFIRMED)))
                    .collect(Collectors.toList())));
        else {
            newConfirmedRequests.addAll(requestService.saveAll(requestsForUpdate.stream()
                    .limit(availableLimit).peek(pr -> pr.setStatus((RequestStatus.CONFIRMED)))
                    .collect(Collectors.toList())));
            newRejectedRequests.addAll(requestService.saveAll(requestsForUpdate
                    .subList(Math.toIntExact(availableLimit), requestsForUpdate.size()).stream()
                    .peek(pr -> pr.setStatus(RequestStatus.REJECTED)).collect(Collectors.toList())));
        }
        return new EventRequestStatusUpdateResult()
                .setConfirmedRequests(newConfirmedRequests.stream()
                        .map(ParticipationRequestMapper::toParticipationRequestDto).collect(Collectors.toList()))
                .setRejectedRequests(newRejectedRequests.stream()
                        .map(ParticipationRequestMapper::toParticipationRequestDto).collect(Collectors.toList()));
    }

    @Override
    public EventFullDto update(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = findById(eventId);
        if (updateEventAdminRequest.getStateAction() != null
                && updateEventAdminRequest.getStateAction().equals(AdminEventStateAction.PUBLISH_EVENT)
                && LocalDateTime.now().plusHours(1).isAfter(event.getEventDate()))
            throw new TooLateToPublishException("Возможность публикации существует ранее чем за час до события.");
        if (!event.getState().equals(EventState.PENDING))
            throw new InvalidEventStateException("Событие должно иметь статус в ожидании.");

        EventState newEventState;
        if (updateEventAdminRequest.getStateAction() == null)
            newEventState = event.getState();
        else if (updateEventAdminRequest.getStateAction().equals(AdminEventStateAction.PUBLISH_EVENT))
            newEventState = EventState.PUBLISHED;
        else
            newEventState = EventState.CANCELED;

        return EventMapper.toEventFullDto(eventRepository.save(event
                        .setAnnotation(updateEventAdminRequest.getAnnotation() == null
                                ? event.getAnnotation()
                                : updateEventAdminRequest.getAnnotation())
                        .setCategory(updateEventAdminRequest.getCategory() == null
                                ? event.getCategory()
                                : categoryService.findById(updateEventAdminRequest.getCategory()))
                        .setDescription(updateEventAdminRequest.getDescription() == null
                                ? event.getDescription()
                                : updateEventAdminRequest.getDescription())
                        .setEventDate(updateEventAdminRequest.getEventDate() == null
                                ? event.getEventDate()
                                : updateEventAdminRequest.getEventDate())
                        .setLocation(updateEventAdminRequest.getLocation() == null
                                ? event.getLocation()
                                : locationService.save(updateEventAdminRequest.getLocation()))
                        .setPaid(updateEventAdminRequest.getPaid() == null
                                ? event.getPaid()
                                : updateEventAdminRequest.getPaid())
                        .setParticipantLimit(updateEventAdminRequest.getParticipantLimit() == null
                                ? event.getParticipantLimit()
                                : updateEventAdminRequest.getParticipantLimit())
                        .setRequestModeration(updateEventAdminRequest.getRequestModeration() == null
                                ? event.getRequestModeration()
                                : updateEventAdminRequest.getRequestModeration())
                        .setState(newEventState)
                        .setTitle(updateEventAdminRequest.getTitle() == null
                                ? event.getTitle()
                                : updateEventAdminRequest.getTitle())),
                requestService.countByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED),
                statsClient.countByEventId(event.getId()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> findAdminByFilters(List<Long> users, List<EventState> states, List<Long> categories,
                                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from,
                                                 Integer size) {
        Pageable page = new OffsetBasedPageRequest(from, size);
        return eventRepository.findAdminByFilters(users, states, categories, rangeStart, rangeEnd, page).stream()
                .map(e -> EventMapper.toEventFullDto(e,
                        requestService.countByEventIdAndStatus(e.getId(), RequestStatus.CONFIRMED),
                        statsClient.countByEventId(e.getId()))).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> findPublicByFilters(String text, Set<Long> categories, Boolean paid,
                                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                   Boolean onlyAvailable, SortType sort, Integer from, Integer size,
                                                   HttpServletRequest request) {

        if (rangeStart != null && rangeEnd != null && rangeEnd.isBefore(rangeStart))
            throw new EndBeforeStartException("Время конца поискового диапазона не может быть раньше времени начала.");
        if (rangeStart == null)
            rangeStart = LocalDateTime.now();
        statsClient.save(new EndpointHit().setApp("ewm-main-service").setUri(request.getRequestURI())
                .setIp(request.getRemoteAddr()));
        List<EventShortDto> dtos = eventRepository.findPublicByFilters(text, categories, paid, rangeStart, rangeEnd,
                        onlyAvailable)
                .stream().map(e -> EventMapper.toEventShortDto(e,
                        requestService.countByEventIdAndStatus(e.getId(), RequestStatus.CONFIRMED),
                        statsClient.countByEventId(e.getId()))).collect(Collectors.toList());

        if (sort == null)
            return dtos.subList(from, Math.min(from + size, dtos.size()));
        else if (sort.equals(SortType.EVENT_DATE))
            dtos.sort(Comparator.comparing(EventShortDto::getEventDate));
        else
            dtos.sort(Comparator.comparing(EventShortDto::getViews));
        return dtos.subList(from, Math.min(from + size, dtos.size()));
    }

    @Override
    public EventFullDto findDtoById(Long eventId, HttpServletRequest request) {
        statsClient.save(new EndpointHit().setApp("ewm-main-service").setUri(request.getRequestURI())
                .setIp(request.getRemoteAddr()));
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException(String.format("Событие с id %d не найдено.", eventId)));
        return EventMapper.toEventFullDto(event,
                requestService.countByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED),
                (long) statsClient.findByStartAndEndAndUrisAndIsUniqueIp(event.getCreatedOn(), LocalDateTime.now(),
                        List.of(request.getRequestURI()), true).size());
    }

    @Override
    public List<Event> findByIdIn(List<Long> eventIds) {
        if (eventIds == null)
            return Collections.emptyList();
        return eventRepository.findByIdIn(eventIds);
    }

    private Event checkEventAndUser(Long userId, Long eventId) {
        User initiator = userService.findById(userId);
        Event event = findById(eventId);
        if (!Objects.equals(event.getInitiator().getId(), initiator.getId()))
            throw new NotFoundException(String.format("Пользователь c id %d не является организатором события с id %d",
                    userId, eventId));
        return event;
    }
}
