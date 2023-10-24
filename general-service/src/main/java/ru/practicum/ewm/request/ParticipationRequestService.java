package ru.practicum.ewm.request;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.RequestStatus;
import ru.practicum.ewm.request.entity.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestService {
    Long countByEventIdAndStatus(Long id, RequestStatus status);

    List<ParticipationRequestDto> findByEventId(Long eventId);

    List<ParticipationRequest> findByStatusAndIdIn(RequestStatus status, List<Long> requestIds);

    List<ParticipationRequest> saveAll(List<ParticipationRequest> participationRequests);

    List<ParticipationRequestDto> findByRequesterId(Long userId);

    ParticipationRequestDto save(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipationRequest> findByIdIn(List<Long> requestIds);
}
