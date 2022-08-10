package com.evgKuznetsov.expert.validation.constraints;

import com.evgKuznetsov.expert.model.AbstractEntity;
import com.evgKuznetsov.expert.validation.EntityIsExistChecking;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = EntityIsExistChecking.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface isExist {
    String message() default "{inconsistent}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends JpaRepository<? extends AbstractEntity, Long>> repository();
}


