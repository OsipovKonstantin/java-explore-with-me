package ru.practicum.ewm.validation;

import ru.practicum.ewm.request.entity.RequestStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OnlyConfirmedOrRejectedRequestStatus implements ConstraintValidator<OnlyConfirmedOrRejected, RequestStatus> {
    @Override
    public void initialize(OnlyConfirmedOrRejected constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(RequestStatus requestStatus, ConstraintValidatorContext constraintValidatorContext) {
        return requestStatus.equals(RequestStatus.CONFIRMED) || requestStatus.equals(RequestStatus.REJECTED);
    }
}