package com.moayong.api.domain.season.enums;

import lombok.Getter;

@Getter
public enum SeasonPeriod {
    Week(7);

    private final int period;

    SeasonPeriod(int period) {
        this.period = period;
    }
}
