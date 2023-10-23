package ru.practicum.ewm.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.category.entity.Category;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.entity.Event;
import ru.practicum.ewm.event.entity.EventState;
import ru.practicum.ewm.event.entity.Location;
import ru.practicum.ewm.user.entity.User;
import ru.practicum.ewm.user.mapper.UserMapper;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {
    public static EventShortDto toEventShortDto(Event event, Long confirmedRequests, Long views) {
        return new EventShortDto()
                .setId(event.getId())
                .setAnnotation(event.getAnnotation())
                .setCategory(CategoryMapper.toCategoryDto(event.getCategory()))
                .setConfirmedRequests(confirmedRequests)
                .setEventDate(event.getEventDate())
                .setInitiator(UserMapper.toUserShortDto(event.getInitiator()))
                .setPaid(event.getPaid())
                .setTitle(event.getTitle())
                .setViews(views);
    }

    public static Event toEvent(NewEventDto newEventDto, User initiator, Category category, Location location) {
        return new Event()
                .setAnnotation(newEventDto.getAnnotation())
                .setCategory(category)
                .setCreatedOn(LocalDateTime.now())
                .setDescription(newEventDto.getDescription())
                .setEventDate(newEventDto.getEventDate())
                .setInitiator(initiator)
                .setLocation(location)
                .setPaid(newEventDto.getPaid())
                .setParticipantLimit(newEventDto.getParticipantLimit())
                .setPublishedOn(null)
                .setRequestModeration(newEventDto.getRequestModeration())
                .setState(EventState.PENDING)
                .setTitle(newEventDto.getTitle());
    }

    public static EventFullDto toEventFullDto(Event event, Long confirmedRequests, Long views) {
        return new EventFullDto()
                .setId(event.getId())
                .setAnnotation(event.getAnnotation())
                .setCategory(CategoryMapper.toCategoryDto(event.getCategory()))
                .setConfirmedRequests(confirmedRequests)
                .setCreatedOn(event.getCreatedOn())
                .setDescription(event.getDescription())
                .setEventDate(event.getEventDate())
                .setInitiator(UserMapper.toUserShortDto(event.getInitiator()))
                .setLocation(LocationMapper.toLocationDto(event.getLocation()))
                .setPaid(event.getPaid())
                .setParticipantLimit(event.getParticipantLimit())
                .setPublishedOn(event.getPublishedOn())
                .setState(event.getState())
                .setTitle(event.getTitle())
                .setViews(views);
    }
}