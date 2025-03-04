package com.moayong.api.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SavingsRateValidator implements ConstraintValidator<SavingsRate, Integer> {
    @Override
    public void initialize(SavingsRate constraintAnnotation) {}

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && value % 10 == 0 && value >= 10 && value <= 60;
    }
}
