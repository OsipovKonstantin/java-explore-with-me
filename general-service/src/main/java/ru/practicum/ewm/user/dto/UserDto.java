package ru.practicum.ewm.user.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class UserDto {
    private final Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String email;
}
