package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.practicum.ewm.event.entity.UserEventStateAction;
import ru.practicum.ewm.validation.AtLeastThoHoursFromNowOn;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.ewm.constants.Constants.DATE_TIME_PATTERN;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@AtLeastThoHoursFromNowOn
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private UserEventStateAction stateAction;

    @Size(min = 3, max = 120)
    private String title;
}