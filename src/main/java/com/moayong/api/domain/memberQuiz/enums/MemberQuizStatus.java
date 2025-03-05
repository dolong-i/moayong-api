package com.moayong.api.domain.memberQuiz.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberQuizStatus {
    SUCCESS(25),
    FAIL(5);

    private final Integer score;
}
