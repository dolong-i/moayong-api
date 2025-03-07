package com.moayong.api.domain.memberquiz.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberQuizStatus {
    CORRECT(25),
    WRONG(5);

    private final Integer score;
}
