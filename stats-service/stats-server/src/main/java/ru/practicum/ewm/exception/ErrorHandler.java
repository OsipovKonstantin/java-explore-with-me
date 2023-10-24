package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({ValidationException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(Exception e) {
        log.debug("{}", e.getMessage());

        return new ApiError(Collections.singletonList(getError(e)),
                e.getLocalizedMessage(),
                "Данные не прошли валидацию (запрос составлен некорректно). Ошибка 400 Bad Request",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(MethodArgumentNotValidException e) {
        log.debug("{}", e.getMessage());

        return new ApiError(e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.toList()),
                e.getLocalizedMessage(),
                "Данные не прошли валидацию (запрос составлен некорректно). Ошибка 400 Bad Request",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleInternalServerErrorException(Throwable e) {
        log.debug("{}", e.getMessage());

        return new ApiError(Collections.singletonList(getError(e)),
                e.getLocalizedMessage(),
                "Произошла непредвиденная ошибка 500 Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getError(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
