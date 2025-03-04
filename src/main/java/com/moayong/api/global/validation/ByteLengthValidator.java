package com.moayong.api.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.nio.charset.StandardCharsets;

public class ByteLengthValidator implements ConstraintValidator<ByteLength, String> {
    private int minLength;
    private int maxLength;

    @Override
    public void initialize(ByteLength constraintAnnotation) {
        this.minLength = constraintAnnotation.min();
        this.maxLength = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null은 허용
        }

        int byteLength = value.getBytes(StandardCharsets.UTF_8).length;

        // 최소 바이트 길이와 최대 바이트 길이를 체크
        return byteLength >= minLength && byteLength <= maxLength;
    }
}