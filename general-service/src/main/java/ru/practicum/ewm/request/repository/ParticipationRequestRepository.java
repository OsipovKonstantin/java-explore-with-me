package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.request.entity.ParticipationRequest;
import ru.practicum.ewm.request.entity.RequestStatus;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    Long countByEventIdAndStatus(Long id, RequestStatus status);

    List<ParticipationRequest> findByEventId(Long eventId);

    List<ParticipationRequest> findByStatusAndIdIn(RequestStatus status, List<Long> requestIds);

    List<ParticipationRequest> findByRequesterId(Long userId);

    List<ParticipationRequest> findByIdIn(List<Long> requestIds);
}
