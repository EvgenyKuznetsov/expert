package com.evgKuznetsov.expert.validation.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotNull(message = "{not_null}")
@Positive(message = "{positive}")
@Constraint(validatedBy = {})
@Target({CONSTRUCTOR, FIELD, METHOD, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface ValidId {

    String message() default "{default}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
