package com.moayong.api.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class DomainSpecificException extends RuntimeException {
    private final String domain;
    private final HttpStatus status;
    private final String code;
    private final Map<String, Object> errorData;

    public DomainSpecificException(String domain, HttpStatus status, Enum<?> errorCode, String message, Map<String, Object> errorData) {
        super(message);
        this.domain = domain;
        this.status = status;
        this.code = errorCode.name();
        this.errorData = errorData != null ? errorData : new HashMap<>();
    }

    public String getLogMessage() {
        String logMessage = String.format("%s exception: %s",
                domain, getMessage());
        if (errorData != null && !errorData.isEmpty()) {
            logMessage += ", data: " + errorData;
        }
        return logMessage;
    }
}