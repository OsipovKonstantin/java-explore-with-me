package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.CommentService;
import ru.practicum.ewm.comment.dto.CommentFullDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdateCommentDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.ewm.constants.Constants.MIN_PAGE_FROM;
import static ru.practicum.ewm.constants.Constants.MIN_PAGE_SIZE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentController {
    private final CommentService commentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentFullDto> findByAuthorId(@PathVariable(name = "userId") Long id,
                                               @Min(MIN_PAGE_FROM) @RequestParam(defaultValue = "0") Integer from,
                                               @Min(MIN_PAGE_SIZE) @RequestParam(defaultValue = "10") Integer size) {
        return commentService.findByAuthorId(id, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullDto save(@PathVariable Long userId,
                               @RequestParam Long eventId,
                               @Valid @RequestBody NewCommentDto newCommentDto) {
        return commentService.save(userId, eventId, newCommentDto);
    }

    @PatchMapping("/{comId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentFullDto update(@PathVariable Long userId,
                                 @PathVariable Long comId,
                                 @Valid @RequestBody UpdateCommentDto updateCommentDto) {
        return commentService.update(userId, comId, updateCommentDto);
    }

    @PatchMapping("/{comId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public void cancel(@PathVariable Long userId,
                       @PathVariable Long comId) {
        commentService.cancel(userId, comId);
    }
}
