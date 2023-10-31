package ru.practicum.ewm.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.comment.dto.CommentState;
import ru.practicum.ewm.comment.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByAuthorIdAndStateNot(Long id, CommentState state, Pageable page);

    boolean existsByIdAndStateNot(Long id, CommentState state);

    @Query(value = "select c " +
            "from Comment c " +
            "where (nullif((:events), null) is null or c.event.id in :events) " +
            "and (nullif((:rangeStart), null) is null or c.createdOn > :rangeStart) " +
            "and (nullif((:rangeEnd), null) is null or c.createdOn < :rangeEnd) " +
            "and (nullif((:states), null) is null or c.state in :states) " +
            "and (nullif((:users), null) is null or c.author.id in :users)")
    Page<Comment> findByAdminFilters(List<Long> events, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                     List<CommentState> states, List<Long> users, Pageable page);

    Long countByEventIdAndStateNot(Long eventId, CommentState state);
}
