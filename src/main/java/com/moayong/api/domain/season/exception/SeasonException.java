package com.moayong.api.domain.season.exception;
import com.moayong.api.domain.season.enums.SeasonErrorCode;
import com.moayong.api.global.exception.DomainSpecificException;
import lombok.Getter;

import java.util.Map;

@Getter
public class SeasonException extends DomainSpecificException {
    public SeasonException(SeasonErrorCode errorCode, String message, Map<String, Object> errorData) {
        super("Season", errorCode.getStatus(), errorCode, message, errorData);
    }

    public SeasonException(SeasonErrorCode errorCode) {
        this(errorCode, errorCode.getMessage(), null);
    }

    public SeasonException(SeasonErrorCode errorCode, Map<String, Object> errorData) {
        this(errorCode, errorCode.getMessage(), errorData);
    }

    public SeasonException(SeasonErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }
}