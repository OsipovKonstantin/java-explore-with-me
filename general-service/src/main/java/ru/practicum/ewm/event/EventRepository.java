package ru.practicum.ewm.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.dto.EventState;
import ru.practicum.ewm.event.entity.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByInitiatorId(Long userId, Pageable page);

    @Query("select e " +
            "from Event e " +
            "where (nullif((:users), null) is null or e.initiator.id in (:users)) " +
            "and (nullif((:states), null) is null or e.state in (:states)) " +
            "and (nullif((:categories), null) is null or e.category.id in (:categories)) " +
            "and (nullif((:rangeStart), null) is null or e.eventDate >= :rangeStart) " +
            "and (nullif((:rangeEnd), null) is null or e.eventDate <= :rangeEnd)")
    Page<Event> findAdminByFilters(List<Long> users, List<EventState> states, List<Long> categories,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    @Query("select e " +
            "from Event e " +
            "where e.state = 'PUBLISHED' " +
            "and (nullif((:text), null) is null or (lower(e.annotation) like lower(concat('%', :text, '%')) " +
            "or lower(e.description) like lower(concat('%', :text, '%')))) " +
            "and (nullif((:categories), null) is null or e.category.id in (:categories)) " +
            "and (nullif((:paid), null) is null or e.paid = :paid) " +
            "and e.eventDate > :rangeStart " +
            "and (nullif((:rangeEnd), null) is null or e.eventDate < :rangeEnd) " +
            "and (nullif((:onlyAvailable), null) is null or e.participantLimit > (" +
            "select count(r.id) " +
            "from ParticipationRequest r " +
            "where r.event.id = e.id " +
            "and r.status = 'CONFIRMED'))")
    List<Event> findPublicByFilters(String text, Set<Long> categories, Boolean paid,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable);

    Optional<Event> findByIdAndState(Long eventId, EventState state);

    List<Event> findByIdIn(List<Long> eventIds);

    boolean existsByCategoryId(Long id);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);
}