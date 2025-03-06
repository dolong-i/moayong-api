package com.moayong.api.domain.leaguemember.exception;

import com.moayong.api.domain.leaguemember.enums.LeagueMemberErrorCode;
import com.moayong.api.domain.memberQuiz.enums.MemberQuizErrorCode;
import com.moayong.api.global.exception.DomainSpecificException;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class LeagueMemberException extends DomainSpecificException {
    public LeagueMemberException(LeagueMemberErrorCode errorCode, String message, Map<String, Object> errorData) {
        super("LeagueMember", errorCode.getStatus(), errorCode, message, errorData);
    }

    public LeagueMemberException(LeagueMemberErrorCode errorCode) {
        this(errorCode, errorCode.getMessage(), null);
    }

    public LeagueMemberException(LeagueMemberErrorCode errorCode, Map<String, Object> errorData) {
        this(errorCode, errorCode.getMessage(), errorData);
    }

    public LeagueMemberException(LeagueMemberErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }
}
