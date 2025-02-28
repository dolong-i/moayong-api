package com.moayong.api.domain.league.exception;

import com.moayong.api.domain.league.enums.LeagueErrorCode;
import com.moayong.api.global.exception.DomainSpecificException;
import lombok.Getter;

import java.util.Map;

@Getter
public class LeagueException extends DomainSpecificException {
    public LeagueException(LeagueErrorCode errorCode, String message, Map<String, Object> errorData) {
        super("League", errorCode.getStatus(), errorCode, message, errorData);
    }

    public LeagueException(LeagueErrorCode errorCode) {
        this(errorCode, errorCode.getMessage(), null);
    }

    public LeagueException(LeagueErrorCode errorCode, Map<String, Object> errorData) {
        this(errorCode, errorCode.getMessage(), errorData);
    }

    public LeagueException(LeagueErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }
}
