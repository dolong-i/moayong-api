package com.moayong.api.domain.memberquiz.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberQuizErrorCode {
    SOLVED_QUIZ_NOT_FOUND(HttpStatus.BAD_REQUEST, "사용자가 푼 퀴즈를 찾을 수 없습니다."),
    EMPTY_QUIZ_TO_SOLVE(HttpStatus.BAD_REQUEST, "풀어야할 퀴즈가 없습니다."),
    ALREADY_SOLVED_ASSIGNED_QUIZ(HttpStatus.FORBIDDEN, "오늘 할당된 퀴즈를 모두 풀었습니다.");

    private final HttpStatus status;
    private final String message;
}
