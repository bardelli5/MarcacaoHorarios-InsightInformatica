package com.example.marcacaohorarios.domain.annotation.impl;


import com.example.marcacaohorarios.domain.annotation.Hour;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HourConstraintValidator implements ConstraintValidator<Hour, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (!s.matches("[0-9]{2}:[0-9]{2}")) {
            return false;
        }

        String[] split = s.split(":");

        int hour = Integer.parseInt(split[0]);
        int minute = Integer.parseInt(split[1]);

        if (hour >= 24 || hour < 0) return false;
        return minute < 60 && minute >= 0;
    }
}
