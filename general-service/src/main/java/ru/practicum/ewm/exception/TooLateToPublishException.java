package ru.practicum.ewm.exception;

public class TooLateToPublishException extends RuntimeException {
    public TooLateToPublishException(String message) {
        super(message);
    }
}
