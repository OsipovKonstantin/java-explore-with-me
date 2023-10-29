package ru.practicum.ewm.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.*;
import ru.practicum.ewm.comment.entity.Comment;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.customclasses.OffsetBasedPageRequest;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.entity.Event;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.user.UserService;
import ru.practicum.ewm.user.entity.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final EventMapper eventMapper;
    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    @Override
    public CommentShortDto findShortDtoById(Long id) {
        Comment comment = findById(id);

        Event event = comment.getEvent();
        EventShortDto eventShortDto = eventMapper.toEventShortDto(event);
        return commentMapper.toCommentShortDto(comment, eventShortDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentFullDto> findByAuthorId(Long id, Integer from, Integer size) {
        Pageable page = new OffsetBasedPageRequest(from, size);
        return commentRepository.findByAuthorIdAndStateNot(id, CommentState.CANCELED, page).stream()
                .map(commentMapper::toCommentFullDto).collect(Collectors.toList());
    }

    @Override
    public CommentFullDto save(Long userId, Long eventId, NewCommentDto newCommentDto) {
        return commentMapper.toCommentFullDto(commentRepository.save(commentMapper.toComment(newCommentDto, userId, eventId)));
    }

    @Override
    public CommentFullDto update(Long userId, Long comId, UpdateCommentDto updateCommentDto) {
        Comment comment = checkCommentWithAuthor(userId, comId);
        return commentMapper.toCommentFullDto(commentRepository.save(
                comment.setText(updateCommentDto == null ? comment.getText() : updateCommentDto.getText())
                        .setState(CommentState.EDITED)));
    }

    @Override
    public void cancel(Long userId, Long comId) {
        Comment comment = checkCommentWithAuthor(userId, comId);
        commentRepository.save(comment.setState(CommentState.CANCELED));
    }

    @Override
    public void cancel(Long comId) {
        Comment comment = findById(comId);
        commentRepository.save(comment.setState(CommentState.CANCELED));
    }

    @Override
    public List<CommentFullDto> findByAdminFilters(FindByAdminFiltersParams params) {
        Pageable page = new OffsetBasedPageRequest(params.getFrom(), params.getSize());

        return commentRepository.findByAdminFilters(params.getEvents(), params.getRangeStart(), params.getRangeEnd(),
                        params.getStates(), params.getUsers(), page)
                .stream().map(commentMapper::toCommentFullDto)
                .collect(Collectors.toList());
    }

    private Comment checkCommentWithAuthor(Long userId, Long comId) {
        User author = userService.findById(userId);
        Comment comment = findById(comId);
        if (!Objects.equals(author.getId(), comment.getAuthor().getId()))
            throw new ValidationException(String.format("Пользователь с id %d не является автором комментария с id %d",
                    userId, comId));
        return comment;
    }

    private Comment findById(Long id) {
        checkComment(id);
        return commentRepository.findById(id).get();
    }

    private void checkComment(Long id) {
        if (!commentRepository.existsByIdAndStateNot(id, CommentState.CANCELED))
            throw new NotFoundException(String.format("Комментарий с id %d не найден.", id));
    }
}