package com.moayong.api.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class DomainSpecificException extends RuntimeException {
    private final String domain;
    private final HttpStatus status;

    protected DomainSpecificException(String domain, HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.domain = domain;
    }
}