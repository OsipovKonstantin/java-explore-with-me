package ru.practicum.ewm.exception;

public class NoConfirmationNeeded extends RuntimeException {
    public NoConfirmationNeeded(String message) {
        super(message);
    }
}
