package com.evgKuznetsov.expert.model.validation.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotNull
@Pattern(regexp = "\\d{1,2}\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}", message = "Значение не соответствует требуемому формату х(ххх)ххх-хх-хх")
@Constraint(validatedBy = {})
@Target({CONSTRUCTOR, FIELD, METHOD, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface ValidPhoneNumber {
    String message() default "Значение не соответствует требуемому формату х(ххх)ххх-хх-хх";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
