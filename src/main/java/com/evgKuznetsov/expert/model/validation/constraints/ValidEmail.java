package com.evgKuznetsov.expert.model.validation.constraints;

import org.hibernate.validator.constraints.Length;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotEmpty
@NotNull
@Email
@Length(max = 30, message = "Максимальная длина не должна превышать {max} символов")
@Constraint(validatedBy = {})
@Target({CONSTRUCTOR, FIELD, METHOD, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface ValidEmail {

    String message() default "{value} не соответствует формату e-mail адреса";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
