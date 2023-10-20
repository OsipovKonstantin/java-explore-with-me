package ru.practicum.ewm.compilation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UpdateCompilationRequest {
    private Set<Long> events;
    private Boolean pinned;

    @Size(min = 1, max = 50)
    private String title;
}
