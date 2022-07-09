package com.evgKuznetsov.expert.model.validation.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotNull
@Positive
@Constraint(validatedBy = {})
@Target({CONSTRUCTOR, FIELD, METHOD, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface ValidId {

    String message() default "Не верное значение id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
