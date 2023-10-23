package ru.practicum.ewm.exception;

public class InvalidEventStateException extends RuntimeException {
    public InvalidEventStateException(String message) {
        super(message);
    }
}
