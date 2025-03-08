package com.moayong.api.domain.league.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Tier {
    BRONZE(1, "브론즈", "bronze.png", 30, 70),
    SILVER(2, "실버", "silver.png", 30, 70),
    GOLD(3, "골드", "gold.png", 30, 70),
    PLATINUM(4, "플레", "platinum.png", 30, 70),
    DIAMOND(5, "다이아", "diamond.png", 30, 70);

    private final int level;
    private final String name;
    private final String imageUrl;
    private final int PromotionRate;
    private final int RelegationRate;
    private final int FIRST = 1;
    private final int LAST = 5;

    Tier(int level, String name, String imageUrl, int PromotionRate, int RelegationRate) {
        this.level = level;
        this.name = name;
        this.imageUrl = imageUrl;
        this.PromotionRate = PromotionRate;
        this.RelegationRate = RelegationRate;
    }

    public static Tier findByLevel(int level) {
        return Arrays.stream(values())
                .filter(tier -> tier.level == level)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invallevel tier level: " + level));
    }

    public Tier getNextTier() {
        if (this.level == LAST) {
            return findByLevel(this.level);
        }
        return findByLevel(this.level + 1);
    }

    public Tier getPrevTier() {
        if (this.level == FIRST) {
            return findByLevel(this.level);
        }
        return findByLevel(this.level - 1);
    }
}