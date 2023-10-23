package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({StartAfterEndException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(RuntimeException e) {
        log.debug("{}", e.getMessage());

        return new ApiError(Collections.singletonList(getError(e)),
                e.getLocalizedMessage(),
                "Статус 400 - Bad Request.",
                HttpStatus.BAD_REQUEST);
    }

    private String getError(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
