package com.moayong.api.domain.memberQuiz.exception;

import com.moayong.api.domain.memberQuiz.enums.MemberQuizErrorCode;
import com.moayong.api.global.exception.DomainSpecificException;
import lombok.Getter;

import java.util.Map;

@Getter
public class MemberQuizException extends DomainSpecificException {
    public MemberQuizException(MemberQuizErrorCode errorCode, String message, Map<String, Object> errorData) {
        super("Quiz", errorCode.getStatus(), errorCode, message, errorData);
    }

    public MemberQuizException(MemberQuizErrorCode errorCode) {
        this(errorCode, errorCode.getMessage(), null);
    }

    public MemberQuizException(MemberQuizErrorCode errorCode, Map<String, Object> errorData) {
        this(errorCode, errorCode.getMessage(), errorData);
    }

    public MemberQuizException(MemberQuizErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }
}
