package ru.practicum.ewm.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UpdateCommentDto {
    @Size(min = 1, max = 2000)
    private String text;
}
