package com.example.marcacaohorarios.cross.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public class ValidatorUtils {
    public static<T> Set<ConstraintViolation<T>> validate(T request) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<T>> constraints = validator.validate(request);

        factory.close();

        return constraints;
    }
}
