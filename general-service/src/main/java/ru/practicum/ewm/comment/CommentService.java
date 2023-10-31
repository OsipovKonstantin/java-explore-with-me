package ru.practicum.ewm.comment;

import ru.practicum.ewm.comment.dto.*;

import java.util.List;

public interface CommentService {
    CommentShortDto findShortDtoById(Long id);

    List<CommentFullDto> findByAuthorId(Long id, Integer from, Integer size);

    CommentFullDto save(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentFullDto update(Long userId, Long comId, UpdateCommentDto updateCommentDto);

    void cancel(Long userId, Long comId);

    List<CommentFullDto> findByAdminFilters(FindByAdminFiltersParams params);

    void cancel(Long comId);
}
