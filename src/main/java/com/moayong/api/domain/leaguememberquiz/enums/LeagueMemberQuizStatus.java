package com.moayong.api.domain.leaguememberquiz.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LeagueMemberQuizStatus {
    SUCCESS(25, "SUCCESS"),
    FAIL(5, "FAIL");

    private final Integer score;
    private final String status;
}
