package com.evgKuznetsov.expert.validation.constraints;

import org.hibernate.validator.constraints.Length;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotBlank(message = "{validation.data.not-blank}")
@Length(min = 3, max = 50, message = "{validation.data.length}")
@Constraint(validatedBy = {})
@Target({CONSTRUCTOR, METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface ValidRole {
    String message() default "{default}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
