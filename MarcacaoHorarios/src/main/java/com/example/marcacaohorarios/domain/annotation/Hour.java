package com.example.marcacaohorarios.domain.annotation;

import com.example.marcacaohorarios.domain.annotation.impl.HourConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = HourConstraintValidator.class)
@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Documented
public @interface Hour {
    String message() default
            "Formato de hora inválido, utilizar 'HH:MM'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}