package com.moayong.api.domain.season.exception;

import com.moayong.api.domain.season.enums.SeasonErrorCode;
import com.moayong.api.global.exception.DomainSpecificException;
import lombok.Getter;

@Getter
public class SeasonException extends DomainSpecificException {
    public SeasonException(SeasonErrorCode errorCode) {
        super("League", errorCode.getStatus(), errorCode.getMessage());
    }
}
