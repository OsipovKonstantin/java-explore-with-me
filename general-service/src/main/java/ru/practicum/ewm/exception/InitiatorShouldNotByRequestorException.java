package ru.practicum.ewm.exception;

public class InitiatorShouldNotByRequestorException extends RuntimeException {
    public InitiatorShouldNotByRequestorException(String message) {
        super(message);
    }
}
