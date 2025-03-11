package com.moayong.api.domain.leaguemember.enums;

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
