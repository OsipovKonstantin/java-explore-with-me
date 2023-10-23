package ru.practicum.ewm.exception;

public class StartAfterEndException extends RuntimeException {
    public StartAfterEndException(String message) {
        super(message);
    }
}
