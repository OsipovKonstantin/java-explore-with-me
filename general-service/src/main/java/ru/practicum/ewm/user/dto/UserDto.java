package ru.practicum.ewm.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserDto {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String email;
}
