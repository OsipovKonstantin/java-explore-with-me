package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.entity.Event;
import ru.practicum.ewm.event.entity.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.entity.ParticipationRequest;
import ru.practicum.ewm.request.entity.RequestStatus;
import ru.practicum.ewm.request.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.request.repository.ParticipationRequestRepository;
import ru.practicum.ewm.user.entity.User;
import ru.practicum.ewm.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository requestRepository;
    private final UserService userService;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    @Override
    public Long countByEventIdAndStatus(Long id, RequestStatus status) {
        return requestRepository.countByEventIdAndStatus(id, status);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> findByEventId(Long eventId) {
        return requestRepository.findByEventId(eventId).stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequest> findByStatusAndIdIn(RequestStatus status, List<Long> requestIds) {
        return requestRepository.findByStatusAndIdIn(status, requestIds);
    }

    @Override
    public List<ParticipationRequest> saveAll(List<ParticipationRequest> participationRequests) {
        return requestRepository.saveAll(participationRequests);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> findByRequesterId(Long userId) {
        userService.findById(userId);
        return requestRepository.findByRequesterId(userId).stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto save(Long userId, Long eventId) {
        User requester = userService.findById(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Событие с id %d не найдено.", eventId)));
        if (event.getInitiator().getId().equals(requester.getId()))
            throw new InitiatorShouldNotByRequestorException("Организатор события не может быть запрашивающим участие " +
                    "в событии.");
        if (!event.getState().equals(EventState.PUBLISHED))
            throw new InvalidEventStateException("Заявки на участие принимаются только для опубликованных событий.");
        if (event.getParticipantLimit() - countByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED) <= 0
                && event.getParticipantLimit() != 0)
            throw new ParticipantLimitAlreadyReached("Достигнут лимит заявок.");


        RequestStatus requestStatus = RequestStatus.PENDING;
        if (event.getRequestModeration().equals(false) || event.getParticipantLimit() == 0)
            requestStatus = RequestStatus.CONFIRMED;

        return ParticipationRequestMapper.toParticipationRequestDto(requestRepository.save(
                new ParticipationRequest()
                        .setRequester(requester)
                        .setEvent(event)
                        .setStatus(requestStatus)
                        .setCreated(LocalDateTime.now())));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        User requester = userService.findById(userId);
        ParticipationRequest participationRequest = findById(requestId);
        if (!Objects.equals(requester.getId(), participationRequest.getRequester().getId()))
            throw new AccessDeniedException(String.format("Заявку на участие в событии с id %d может удалить " +
                    "только создатель заявки.", requestId));
        return ParticipationRequestMapper.toParticipationRequestDto(requestRepository
                .save(participationRequest.setStatus(RequestStatus.CANCELED)));
    }

    @Override
    public List<ParticipationRequest> findByIdIn(List<Long> requestIds) {
        return requestRepository.findByIdIn(requestIds);
    }

    private ParticipationRequest findById(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Заявки на участие с id %d не существует.",
                        requestId)));
    }
}
