package com.moayong.api.domain.savings.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SavingsPolicy {
    MAX_SCORE(500);

    private final Integer value;
}
