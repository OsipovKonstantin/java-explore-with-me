package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.CommentService;
import ru.practicum.ewm.comment.dto.CommentShortDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments/")
public class PublicCommentController {
    private final CommentService commentService;

    @GetMapping("/{comId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentShortDto findById(@PathVariable(name = "comId") Long id) {
        return commentService.findShortDtoById(id);
    }
}
