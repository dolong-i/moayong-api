package com.moayong.api.domain.leaguememberquiz.exception;

import com.moayong.api.domain.leaguememberquiz.enums.LeagueMemberQuizErrorCode;
import com.moayong.api.global.exception.DomainSpecificException;
import lombok.Getter;

import java.util.Map;

@Getter
public class LeagueMemberQuizException extends DomainSpecificException {
    public LeagueMemberQuizException(LeagueMemberQuizErrorCode errorCode, String message, Map<String, Object> errorData) {
        super("Quiz", errorCode.getStatus(), errorCode, message, errorData);
    }

    public LeagueMemberQuizException(LeagueMemberQuizErrorCode errorCode) {
        this(errorCode, errorCode.getMessage(), null);
    }

    public LeagueMemberQuizException(LeagueMemberQuizErrorCode errorCode, Map<String, Object> errorData) {
        this(errorCode, errorCode.getMessage(), errorData);
    }

    public LeagueMemberQuizException(LeagueMemberQuizErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }
}
