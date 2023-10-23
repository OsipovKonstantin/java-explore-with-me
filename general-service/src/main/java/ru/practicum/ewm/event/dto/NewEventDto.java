package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.practicum.ewm.validation.AtLeastThoHoursFromNowOn;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.ewm.constants.Constants.DATE_TIME_PATTERN;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@AtLeastThoHoursFromNowOn
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long category;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    @NotNull
    private LocationDto location;

    private Boolean paid = false;
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}
