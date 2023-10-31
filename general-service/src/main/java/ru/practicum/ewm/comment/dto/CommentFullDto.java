package ru.practicum.ewm.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static ru.practicum.ewm.constants.Constants.DATE_TIME_PATTERN;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CommentFullDto {
    private Long id;

    @NotNull
    private EventFullDto event;

    @NotNull
    private UserDto author;

    @NotNull
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime createdOn;

    @NotBlank
    private String text;

    @NotNull
    private CommentState state;
}
