package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.CommentService;
import ru.practicum.ewm.comment.dto.CommentFullDto;
import ru.practicum.ewm.comment.dto.CommentState;
import ru.practicum.ewm.comment.dto.FindByAdminFiltersParams;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.constants.Constants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class AdminCommentController {
    private final CommentService commentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentFullDto> findByAdminFilters(@RequestParam(required = false) List<Long> events,
                                                   @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN)
                                                   LocalDateTime rangeStart,
                                                   @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN)
                                                   LocalDateTime rangeEnd,
                                                   @RequestParam(required = false) List<CommentState> states,
                                                   @RequestParam(required = false) List<Long> users,
                                                   @RequestParam(defaultValue = "0") @Min(MIN_PAGE_FROM) Integer from,
                                                   @RequestParam(defaultValue = "10") @Min(MIN_PAGE_SIZE) Integer size) {
        return commentService.findByAdminFilters(new FindByAdminFiltersParams(events, rangeStart, rangeEnd, states, users, from, size));
    }

    @PatchMapping("/{comId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public void cancel(@PathVariable Long comId) {
        commentService.cancel(comId);
    }
}
