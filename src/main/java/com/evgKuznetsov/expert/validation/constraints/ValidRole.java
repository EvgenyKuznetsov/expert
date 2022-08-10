package com.evgKuznetsov.expert.validation.constraints;

import org.hibernate.validator.constraints.Length;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotNull(message = "{not_null}")
@NotBlank(message = "{not_blank}")
@Length(min = 3, max = 50, message = "{length}")
@Constraint(validatedBy = {})
@Target({CONSTRUCTOR, METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface ValidRole {
    String message() default "{default}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
