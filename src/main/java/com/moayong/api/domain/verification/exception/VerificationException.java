package com.moayong.api.domain.verification.exception;
import com.moayong.api.domain.verification.enums.VerificationErrorCode;
import com.moayong.api.global.exception.DomainSpecificException;
import lombok.Getter;

import java.util.Map;

@Getter
public class VerificationException extends DomainSpecificException {
    public VerificationException(VerificationErrorCode errorCode, String message, Map<String, Object> errorData) {
        super("Verification", errorCode.getStatus(), errorCode, message, errorData);
    }

    public VerificationException(VerificationErrorCode errorCode) {
        this(errorCode, errorCode.getMessage(), null);
    }

    public VerificationException(VerificationErrorCode errorCode, Map<String, Object> errorData) {
        this(errorCode, errorCode.getMessage(), errorData);
    }

    public VerificationException(VerificationErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }
}