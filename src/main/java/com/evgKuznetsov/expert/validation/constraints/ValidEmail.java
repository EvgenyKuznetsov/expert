package com.evgKuznetsov.expert.validation.constraints;

import org.hibernate.validator.constraints.Length;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotNull(message = "{not_null}")
@Email(message = "{email_format}")
@Length(min = 5, max = 30, message = "{length}")
@Constraint(validatedBy = {})
@Target({CONSTRUCTOR, FIELD, METHOD, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface ValidEmail {

    String message() default "{default}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
