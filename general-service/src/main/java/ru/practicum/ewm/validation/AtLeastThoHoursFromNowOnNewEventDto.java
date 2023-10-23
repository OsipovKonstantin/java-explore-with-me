package ru.practicum.ewm.validation;

import ru.practicum.ewm.event.dto.NewEventDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class AtLeastThoHoursFromNowOnNewEventDto implements ConstraintValidator<AtLeastThoHoursFromNowOn, NewEventDto> {
    @Override
    public void initialize(AtLeastThoHoursFromNowOn constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(NewEventDto newEventDto, ConstraintValidatorContext constraintValidatorContext) {
        if (newEventDto.getEventDate() == null)
            return false;
        return newEventDto.getEventDate().isAfter(LocalDateTime.now().plusHours(2));
    }
}