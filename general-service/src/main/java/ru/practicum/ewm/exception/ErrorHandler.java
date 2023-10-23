package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

@Slf4j
@RestControllerAdvice("ru.practicum.ewm")
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException e) {
        log.debug("{}", e.getMessage());

        return new ApiError(Collections.singletonList(getError(e)),
                e.getLocalizedMessage(),
                "Не найдено.",
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidEventStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleInvalidEventStateException(InvalidEventStateException e) {
        log.debug("{}", e.getMessage());

        return new ApiError(Collections.singletonList(getError(e)),
                e.getLocalizedMessage(),
                "Неверное состояние события.",
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidRequestStatusException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleInvalidRequestStatusException(InvalidRequestStatusException e) {
        log.debug("{}", e.getMessage());

        return new ApiError(Collections.singletonList(getError(e)),
                e.getLocalizedMessage(),
                "Неверный статус запроса на участие.",
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ParticipantLimitAlreadyReached.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleParticipantLimitAlreadyReached(ParticipantLimitAlreadyReached e) {
        log.debug("{}", e.getMessage());

        return new ApiError(Collections.singletonList(getError(e)),
                e.getLocalizedMessage(),
                "Достигнут лимит одобренных заявок.",
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TooLateToPublishException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleTooLateToPublishException(TooLateToPublishException e) {
        log.debug("{}", e.getMessage());

        return new ApiError(Collections.singletonList(getError(e)),
                e.getLocalizedMessage(),
                "Можно публиковать раньше, чем за час до начала события.",
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.debug("{}", e.getMessage());

        return new ApiError(Collections.singletonList(getError(e)),
                e.getLocalizedMessage(),
                "Событие не удовлетворяет правилам редактирования.",
                HttpStatus.CONFLICT);
    }


    @ExceptionHandler(InitiatorShouldNotByRequestorException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleInitiatorShouldNotByRequestorException(InitiatorShouldNotByRequestorException e) {
        log.debug("{}", e.getMessage());

        return new ApiError(Collections.singletonList(getError(e)),
                e.getLocalizedMessage(),
                "Организатор не может делать запрос на участие в событии.",
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CategoryHasEventsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleInitiatorCategoryHasEventsException(CategoryHasEventsException e) {
        log.debug("{}", e.getMessage());

        return new ApiError(Collections.singletonList(getError(e)),
                e.getLocalizedMessage(),
                "Категорию, у которой есть события, невозможно удалить.",
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.debug("{}", e.getMessage());

        return new ApiError(Collections.singletonList(getError(e)),
                e.getLocalizedMessage(),
                "Данные не прошли валидацию.",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoConfirmationNeeded.class, EndBeforeStartException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(RuntimeException e) {
        log.debug("{}", e.getMessage());

        return new ApiError(Collections.singletonList(getError(e)),
                e.getLocalizedMessage(),
                "Запрос составлен некорректно.",
                HttpStatus.BAD_REQUEST);
    }

    private String getError(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
