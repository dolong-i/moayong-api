package com.moayong.api.domain.memberquiz.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberQuizErrorCode {
    EMPTY_QUIZ_TO_SOLVE(HttpStatus.BAD_REQUEST, "풀어야할 퀴즈가 없습니다."),
    QUIZ_NOT_IN_PROGRESS(HttpStatus.FORBIDDEN, "진행중인 퀴즈 외에는 접근할 수 없습니다."),
    ALREADY_SOLVED_ASSIGNED_QUIZ(HttpStatus.FORBIDDEN, "오늘 할당된 퀴즈를 모두 풀었습니다."),
    ALREADY_SUBMITTED(HttpStatus.CONFLICT, "이미 제출한 퀴즈입니다."),
    QUIZ_NOT_SOLVED(HttpStatus.FORBIDDEN, "제출한 적 없는 퀴즈입니다.");

    private final HttpStatus status;
    private final String message;
}
