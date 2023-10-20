package ru.practicum.ewm.compilation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.practicum.ewm.event.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CompilationDto {
    @NotNull
    private Long id;

    private Set<EventShortDto> events;

    @NotNull
    private Boolean pinned;

    @NotBlank
    private String title;
}
