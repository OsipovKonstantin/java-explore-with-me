package ru.practicum.ewm.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CommentShortDto {
    private Long id;

    @NotNull
    private EventShortDto event;

    @NotNull
    private UserShortDto author;

    @NotBlank
    private String text;
}
