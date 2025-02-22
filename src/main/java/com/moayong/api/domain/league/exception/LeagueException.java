package com.moayong.api.domain.league.exception;

import com.moayong.api.domain.league.enums.LeagueErrorCode;
import com.moayong.api.global.exception.DomainSpecificException;

public class LeagueException extends DomainSpecificException {
    public LeagueException(LeagueErrorCode errorCode) {
        super("League", errorCode.getStatus(), errorCode.getMessage());
    }
}
