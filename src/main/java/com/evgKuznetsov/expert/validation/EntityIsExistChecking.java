package com.evgKuznetsov.expert.validation;

import com.evgKuznetsov.expert.model.AbstractEntity;
import com.evgKuznetsov.expert.validation.constraints.isExist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

@Slf4j
public class EntityIsExistChecking implements ConstraintValidator<isExist, AbstractEntity> {

    @Autowired
    private WebApplicationContext wac;
    private JpaRepository<? extends AbstractEntity, Long> repository;

    @Override
    public void initialize(isExist constraintAnnotation) {
        this.repository = wac.getBean(constraintAnnotation.repository());
    }

    @Override
    public boolean isValid(AbstractEntity value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true;
        }

        if (Objects.isNull(value.getId())) {
            return false;
        }

        return repository.existsById(value.getId());
    }
}
