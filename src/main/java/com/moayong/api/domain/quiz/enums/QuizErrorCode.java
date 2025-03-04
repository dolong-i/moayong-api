package com.moayong.api.domain.quiz.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QuizErrorCode {
    QUIZ_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "퀴즈를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
