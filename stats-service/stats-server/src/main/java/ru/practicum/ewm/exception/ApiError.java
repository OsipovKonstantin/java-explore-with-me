package ru.practicum.ewm.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.constants.Constants.DATE_TIME_PATTERN;

@Data
public class ApiError {
    private final List<String> errors;
    private final String message;
    private final String reason;
    private final HttpStatus status;

    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private final LocalDateTime timestamp = LocalDateTime.now();
}
