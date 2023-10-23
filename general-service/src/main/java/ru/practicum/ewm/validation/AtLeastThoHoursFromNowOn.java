package ru.practicum.ewm.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
        ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {AtLeastThoHoursFromNowOnNewEventDto.class, AtLeastThoHoursFromNowOnUpdateEventUserRequest.class})
public @interface AtLeastThoHoursFromNowOn {
    String message() default "Время начала события должно быть минимум через 2 часа от текущего момента.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}