package ru.practicum.ewm.exception.validation;

import ru.practicum.ewm.event.dto.UpdateEventUserRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class AtLeastThoHoursFromNowOnUpdateEventUserRequest implements ConstraintValidator<AtLeastThoHoursFromNowOn, UpdateEventUserRequest> {
    @Override
    public void initialize(AtLeastThoHoursFromNowOn constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UpdateEventUserRequest updateEventUserRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (updateEventUserRequest.getEventDate() == null)
            return true;
        return updateEventUserRequest.getEventDate().isAfter(LocalDateTime.now().plusHours(2));
    }
}