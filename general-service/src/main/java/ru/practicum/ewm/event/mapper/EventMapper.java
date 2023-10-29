package ru.practicum.ewm.event.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.CategoryService;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.comment.CommentRepository;
import ru.practicum.ewm.comment.dto.CommentState;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.EventState;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.entity.Event;
import ru.practicum.ewm.location.LocationService;
import ru.practicum.ewm.location.mapper.LocationMapper;
import ru.practicum.ewm.request.ParticipationRequestService;
import ru.practicum.ewm.request.dto.RequestStatus;
import ru.practicum.ewm.user.UserService;
import ru.practicum.ewm.user.mapper.UserMapper;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final ParticipationRequestService requestService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final LocationService locationService;
    private final StatsClient statsClient;
    private final LocationMapper locationMapper;
    private final UserMapper userMapper;
    private final CommentRepository commentRepository;
    private final CategoryMapper categoryMapper;

    public EventShortDto toEventShortDto(Event event) {
        return new EventShortDto()
                .setId(event.getId())
                .setAnnotation(event.getAnnotation())
                .setCategory(categoryMapper.toCategoryDto(event.getCategory()))
                .setConfirmedRequests(requestService.countByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED))
                .setEventDate(event.getEventDate())
                .setInitiator(userMapper.toUserShortDto(event.getInitiator()))
                .setPaid(event.getPaid())
                .setTitle(event.getTitle())
                .setViews(statsClient.countByEventId(event.getId()))
                .setComments(commentRepository.countByEventIdAndStateNot(event.getId(), CommentState.CANCELED));
    }

    public Event toEvent(NewEventDto newEventDto, Long userId) {
        return new Event()
                .setAnnotation(newEventDto.getAnnotation())
                .setCategory(categoryService.findById(newEventDto.getCategory()))
                .setCreatedOn(LocalDateTime.now())
                .setDescription(newEventDto.getDescription())
                .setEventDate(newEventDto.getEventDate())
                .setInitiator(userService.findById(userId))
                .setLocation(locationService.save(newEventDto.getLocation()))
                .setPaid(newEventDto.getPaid())
                .setParticipantLimit(newEventDto.getParticipantLimit())
                .setPublishedOn(null)
                .setRequestModeration(newEventDto.getRequestModeration())
                .setState(EventState.PENDING)
                .setTitle(newEventDto.getTitle());
    }

    public EventFullDto toEventFullDto(Event event) {
        return new EventFullDto()
                .setId(event.getId())
                .setAnnotation(event.getAnnotation())
                .setCategory(categoryMapper.toCategoryDto(event.getCategory()))
                .setConfirmedRequests(requestService.countByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED))
                .setCreatedOn(event.getCreatedOn())
                .setDescription(event.getDescription())
                .setEventDate(event.getEventDate())
                .setInitiator(userMapper.toUserShortDto(event.getInitiator()))
                .setLocation(locationMapper.toLocationDto(event.getLocation()))
                .setPaid(event.getPaid())
                .setParticipantLimit(event.getParticipantLimit())
                .setPublishedOn(event.getPublishedOn())
                .setRequestModeration(event.getRequestModeration())
                .setState(event.getState())
                .setTitle(event.getTitle())
                .setViews(statsClient.countByEventId(event.getId()))
                .setComments(commentRepository.countByEventIdAndStateNot(event.getId(), CommentState.CANCELED));
    }

    public EventFullDto toEventFullDto(Event event, long views) {
        return new EventFullDto()
                .setId(event.getId())
                .setAnnotation(event.getAnnotation())
                .setCategory(categoryMapper.toCategoryDto(event.getCategory()))
                .setConfirmedRequests(requestService.countByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED))
                .setCreatedOn(event.getCreatedOn())
                .setDescription(event.getDescription())
                .setEventDate(event.getEventDate())
                .setInitiator(userMapper.toUserShortDto(event.getInitiator()))
                .setLocation(locationMapper.toLocationDto(event.getLocation()))
                .setPaid(event.getPaid())
                .setParticipantLimit(event.getParticipantLimit())
                .setPublishedOn(event.getPublishedOn())
                .setRequestModeration(event.getRequestModeration())
                .setState(event.getState())
                .setTitle(event.getTitle())
                .setViews(views)
                .setComments(commentRepository.countByEventIdAndStateNot(event.getId(), CommentState.CANCELED));
    }
}