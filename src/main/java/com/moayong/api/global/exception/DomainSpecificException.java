package com.moayong.api.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class DomainSpecificException extends RuntimeException {
    private final String domain;
    private final HttpStatus status;
    private final String customMessage;

    protected DomainSpecificException(String domain, HttpStatus status, String customMessage) {
        this.customMessage = customMessage;
        this.status = status;
        this.domain = domain;
    }
}