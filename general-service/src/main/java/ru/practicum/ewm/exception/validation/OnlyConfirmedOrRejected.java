package ru.practicum.ewm.exception.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
        ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {OnlyConfirmedOrRejectedRequestStatus.class})
public @interface OnlyConfirmedOrRejected {
    String message() default "Статус заявки на участие в событие может быть изменён на подтверждённый или отклонённый.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}