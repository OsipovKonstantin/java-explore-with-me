package ru.practicum.ewm.exception;

public class ParticipantLimitAlreadyReached extends RuntimeException {
    public ParticipantLimitAlreadyReached(String message) {
        super(message);
    }
}
