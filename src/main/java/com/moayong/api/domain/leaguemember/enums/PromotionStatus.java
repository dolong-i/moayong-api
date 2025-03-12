package com.moayong.api.domain.leaguemember.enums;

import com.moayong.api.domain.league.domain.League;

public enum PromotionStatus {
    PROMOTION, RELEGATION, SUSPENDED;

    public static PromotionStatus getStatus(Integer now, Integer next) {
        if (next > now) {
            return PROMOTION;
        } else if (next < now) {
            return RELEGATION;
        } else {
            return SUSPENDED;
        }
    }
}
