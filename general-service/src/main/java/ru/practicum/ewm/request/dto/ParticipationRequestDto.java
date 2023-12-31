package ru.practicum.ewm.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

import static ru.practicum.ewm.constants.Constants.DATE_TIME_PATTERN_MILIS;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ParticipationRequestDto {
    private Long id;
    private Long requester;
    private Long event;
    private RequestStatus status;

    @JsonFormat(pattern = DATE_TIME_PATTERN_MILIS)
    private LocalDateTime created;
}