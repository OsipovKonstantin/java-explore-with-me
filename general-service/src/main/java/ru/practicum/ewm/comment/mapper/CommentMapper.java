package ru.practicum.ewm.comment.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.comment.dto.CommentFullDto;
import ru.practicum.ewm.comment.dto.CommentShortDto;
import ru.practicum.ewm.comment.dto.CommentState;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.entity.Comment;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.EventState;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.user.UserService;
import ru.practicum.ewm.user.mapper.UserMapper;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final UserMapper userMapper;
    private final UserService userService;
    private final EventService eventService;
    private final EventMapper eventMapper;

    public CommentShortDto toCommentShortDto(Comment comment, EventShortDto eventShortDto) {
        return new CommentShortDto()
                .setId(comment.getId())
                .setEvent(eventShortDto)
                .setAuthor(userMapper.toUserShortDto(comment.getAuthor()))
                .setText(comment.getText());
    }

    public CommentFullDto toCommentFullDto(Comment comment) {
        return new CommentFullDto()
                .setId(comment.getId())
                .setEvent(eventMapper.toEventFullDto(comment.getEvent()))
                .setAuthor(userMapper.toUserDto(comment.getAuthor()))
                .setCreatedOn(comment.getCreatedOn())
                .setText(comment.getText())
                .setState(comment.getState());
    }

    public Comment toComment(NewCommentDto newCommentDto, Long userId, Long eventId) {
        return new Comment()
                .setEvent(eventService.findByIdAndState(eventId, EventState.PUBLISHED))
                .setAuthor(userService.findById(userId))
                .setCreatedOn(LocalDateTime.now())
                .setText(newCommentDto.getText())
                .setState(CommentState.CREATED);
    }
}
