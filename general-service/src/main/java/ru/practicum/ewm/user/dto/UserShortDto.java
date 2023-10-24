package ru.practicum.ewm.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserShortDto {
    @NotNull
    private Long id;

    @NotBlank
    private String name;
}
