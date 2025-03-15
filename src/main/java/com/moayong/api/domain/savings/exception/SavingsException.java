package com.moayong.api.domain.savings.exception;

import com.moayong.api.domain.savings.enums.SavingsErrorCode;
import com.moayong.api.global.exception.DomainSpecificException;

import java.util.Map;

public class SavingsException extends DomainSpecificException {
    public SavingsException(SavingsErrorCode errorCode, String message, Map<String, Object> errorData) {
        super("Savings", errorCode.getStatus(), errorCode, message, errorData);
    }

    public SavingsException(SavingsErrorCode errorCode) {
        this(errorCode, errorCode.getMessage(), null);
    }

    public SavingsException(SavingsErrorCode errorCode, Map<String, Object> errorData) {
        this(errorCode, errorCode.getMessage(), errorData);
    }

    public SavingsException(SavingsErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }
}
