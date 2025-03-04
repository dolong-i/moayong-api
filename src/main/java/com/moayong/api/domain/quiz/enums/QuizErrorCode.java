package com.moayong.api.domain.quiz.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QuizErrorCode {
    QUIZ_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "퀴즈를 찾을 수 없습니다."),
    QUIZ_OPTIONS_CONVERTING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "퀴즈 옵션 컨버팅 중 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;
}
