package ru.practicum.ewm.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class FindByAdminFiltersParams {
    private List<Long> events;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private List<CommentState> states;
    private List<Long> users;
    private Integer from;
    private Integer size;
}
