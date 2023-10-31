package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.CategoryService;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.customclasses.OffsetBasedPageRequest;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.entity.Event;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.location.LocationService;
import ru.practicum.ewm.location.mapper.LocationMapper;
import ru.practicum.ewm.request.ParticipationRequestService;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.RequestStatus;
import ru.practicum.ewm.request.entity.ParticipationRequest;
import ru.practicum.ewm.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.user.UserService;

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
    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;
    private final ParticipationRequestMapper participationRequestMapper;

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> findByInitiatorId(Long userId, Integer from, Integer size) {
        Pageable page = new OffsetBasedPageRequest(from, size);
        userService.checkExists(userId);

        return eventRepository.findByInitiatorId(userId, page).stream().map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto save(Long userId, NewEventDto newEventDto) {
        return eventMapper.toEventFullDto(eventRepository.save(eventMapper.toEvent(newEventDto, userId)));
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto findByIdAndInitiatorId(Long userId, Long eventId) {
        Event event = checkEventWithInitiatorExists(eventId, userId);

        return eventMapper.toEventFullDto(event);
    }

    @Transactional(readOnly = true)
    @Override
    public Event findById(Long eventId) {
        checkExists(eventId);
        return eventRepository.findById(eventId).get();
    }

    private void checkExists(Long id) {
        if (!eventRepository.existsById(id))
            throw new NotFoundException(String.format("Событие с id %d не найдено.", id));
    }

    @Override
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = checkEventWithInitiatorExists(eventId, userId);
        if (!event.getState().equals(EventState.CANCELED) && !event.getState().equals(EventState.PENDING))
            throw new ConflictException("Статус события должен быть отмененным или в ожидании.");

        EventState newState;
        if (updateEventUserRequest.getStateAction() == null)
            newState = event.getState();
        else if (updateEventUserRequest.getStateAction().equals(UserEventStateAction.SEND_TO_REVIEW))
            newState = EventState.PENDING;
        else
            newState = EventState.CANCELED;

        return eventMapper.toEventFullDto(eventRepository.save(event
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
                        : locationMapper.toLocation(updateEventUserRequest.getLocation()))
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
                        : updateEventUserRequest.getTitle())));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> findParticipationRequestsByEventId(Long userId, Long eventId) {
        checkEventWithInitiatorExists(eventId, userId);
        return requestService.findByEventId(eventId);
    }

    @Override
    public EventRequestStatusUpdateResult updateParticipationRequestsStatus(Long userId, Long eventId,
                                                                            EventRequestStatusUpdateRequest
                                                                                    eventRequestStatusUpdateRequest) {
        Event event = checkEventWithInitiatorExists(eventId, userId);
        if (event.getParticipantLimit() == 0 || Boolean.FALSE.equals(event.getRequestModeration()))
            throw new ValidationException("Подтверждение заявки на участие в событии не требуется " +
                    "при отсутствии лимита заявок или выключенной пре-модерации.");

        List<ParticipationRequest> requestsForUpdate = requestService
                .findByIdIn(eventRequestStatusUpdateRequest.getRequestIds());
        if (requestsForUpdate.stream().anyMatch(pr -> pr.getStatus() != RequestStatus.PENDING))
            throw new ConflictException("Все обновляемые заявки на участие в событии должны иметь статус " +
                    "в ожидании.");

        List<ParticipationRequest> newConfirmedRequests = new ArrayList<>();
        List<ParticipationRequest> newRejectedRequests = new ArrayList<>();

        if (eventRequestStatusUpdateRequest.getStatus().equals(RequestStatus.REJECTED)) {
            newRejectedRequests.addAll(requestService.saveAll(requestsForUpdate.stream()
                    .peek(pr -> pr.setStatus(RequestStatus.REJECTED)).collect(Collectors.toList())));
            return new EventRequestStatusUpdateResult()
                    .setConfirmedRequests(List.of())
                    .setRejectedRequests(newRejectedRequests.stream()
                            .map(participationRequestMapper::toParticipationRequestDto).collect(Collectors.toList()));
        }

        Long confirmedRequests = requestService.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
        Integer participantLimit = event.getParticipantLimit();
        Long availableLimit = participantLimit - confirmedRequests;
        if (availableLimit <= 0)
            throw new ConflictException("Достигнут лимит заявок.");

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
                        .map(participationRequestMapper::toParticipationRequestDto).collect(Collectors.toList()))
                .setRejectedRequests(newRejectedRequests.stream()
                        .map(participationRequestMapper::toParticipationRequestDto).collect(Collectors.toList()));
    }

    @Override
    public EventFullDto update(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = findById(eventId);
        if (updateEventAdminRequest.getStateAction() != null
                && updateEventAdminRequest.getStateAction().equals(AdminEventStateAction.PUBLISH_EVENT)
                && LocalDateTime.now().plusHours(1).isAfter(event.getEventDate()))
            throw new ConflictException("Возможность публикации существует ранее чем за час до события.");
        if (!event.getState().equals(EventState.PENDING))
            throw new ConflictException("Событие должно иметь статус в ожидании.");

        EventState newEventState;
        if (updateEventAdminRequest.getStateAction() == null)
            newEventState = event.getState();
        else if (updateEventAdminRequest.getStateAction().equals(AdminEventStateAction.PUBLISH_EVENT))
            newEventState = EventState.PUBLISHED;
        else
            newEventState = EventState.CANCELED;

        return eventMapper.toEventFullDto(eventRepository.save(event
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
                        : updateEventAdminRequest.getTitle())));
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> findByAdminFilters(FindByAdminFiltersParams params) {
        Pageable page = new OffsetBasedPageRequest(params.getFrom(), params.getSize());
        return eventRepository.findByAdminFilters(params.getUsers(), params.getStates(), params.getCategories(),
                        params.getRangeStart(), params.getRangeEnd(), page).stream()
                .map(eventMapper::toEventFullDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> findByPublicFilters(FindByPublicFiltersParams params) {
        LocalDateTime rangeStart = params.getRangeStart();
        LocalDateTime rangeEnd = params.getRangeEnd();
        HttpServletRequest request = params.getRequest();
        String text = params.getText();
        Set<Long> categories = params.getCategories();
        Boolean paid = params.getPaid();
        Boolean onlyAvailable = params.getOnlyAvailable();
        SortType sort = params.getSort();
        Integer from = params.getFrom();
        Integer size = params.getSize();

        if (rangeStart != null && rangeEnd != null && rangeEnd.isBefore(rangeStart))
            throw new ValidationException("Время конца поискового диапазона не может быть раньше времени начала.");
        if (rangeStart == null)
            rangeStart = LocalDateTime.now();
        statsClient.save(new EndpointHit().setApp("ewm-main-service").setUri(request.getRequestURI())
                .setIp(request.getRemoteAddr()));
        List<EventShortDto> dtos = eventRepository.findByPublicFilters(text, categories, paid, rangeStart, rangeEnd,
                        onlyAvailable)
                .stream().map(eventMapper::toEventShortDto).collect(Collectors.toList());

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
        Event event = findByIdAndState(eventId, EventState.PUBLISHED);

        long views = statsClient.findByStartAndEndAndUrisAndIsUniqueIp(event.getCreatedOn(), LocalDateTime.now(),
                List.of(request.getRequestURI()), true).size();
        return eventMapper.toEventFullDto(event, views);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Event> findByIdIn(List<Long> eventIds) {
        if (eventIds == null)
            return Collections.emptyList();
        return eventRepository.findByIdIn(eventIds);
    }

    @Override
    public Event findByIdAndState(Long eventId, EventState state) {
        return eventRepository.findByIdAndState(eventId, state).orElseThrow(() ->
                new NotFoundException(String.format("События со статусом %s и id %d не найдено", state, eventId)));
    }

    private Event checkEventWithInitiatorExists(Long eventId, Long userId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(
                () -> new NotFoundException(String.format("Не найдено событие с id %d и организатором с id %d",
                        eventId, userId)));
    }
}