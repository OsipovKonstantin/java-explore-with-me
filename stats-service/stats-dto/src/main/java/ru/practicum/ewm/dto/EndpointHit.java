package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.ewm.constants.Constants.DATE_TIME_PATTERN;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@AllArgsConstructor
public class EndpointHit {
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String app;
    @NotBlank
    @Size(max = 2048)
    private String uri;
    @NotBlank
    @Size(max = 45)
    private String ip;

    @NotNull
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private final LocalDateTime timestamp = LocalDateTime.now();
}
