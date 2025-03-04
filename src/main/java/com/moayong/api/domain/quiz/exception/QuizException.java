package com.moayong.api.domain.quiz.exception;

import com.moayong.api.domain.quiz.enums.QuizErrorCode;
import com.moayong.api.global.exception.DomainSpecificException;
import lombok.Getter;

import java.util.Map;

@Getter
public class QuizException extends DomainSpecificException {
    public QuizException(QuizErrorCode errorCode, String message, Map<String, Object> errorData) {
        super("Quiz", errorCode.getStatus(), errorCode, message, errorData);
    }

    public QuizException(QuizErrorCode errorCode) {
        this(errorCode, errorCode.getMessage(), null);
    }

    public QuizException(QuizErrorCode errorCode, Map<String, Object> errorData) {
        this(errorCode, errorCode.getMessage(), errorData);
    }

    public QuizException(QuizErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }
}
