package com.moayong.api.domain.auth.exception;

import com.moayong.api.domain.auth.enums.AuthErrorCode;
import com.moayong.api.global.exception.DomainSpecificException;
import lombok.Getter;

import java.util.Map;

@Getter
public class AuthException extends DomainSpecificException {
    public AuthException(AuthErrorCode errorCode, String message, Map<String, Object> errorData) {
        super("Auth", errorCode.getStatus(), errorCode, message, errorData);
    }

    public AuthException(AuthErrorCode errorCode) {
        this(errorCode, errorCode.getMessage(), null);
    }

    public AuthException(AuthErrorCode errorCode, Map<String, Object> errorData) {
        this(errorCode, errorCode.getMessage(), errorData);
    }

    public AuthException(AuthErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }
}