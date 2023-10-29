package ru.practicum.ewm.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class NewCommentDto {
    @NotBlank
    @Size(min = 1, max = 2000)
    private String text;
}
