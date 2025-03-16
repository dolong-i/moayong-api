package com.moayong.api.domain.league.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Tier {
    BRONZE(1, "아가용", "https://d3t251u9x9cmf6.cloudfront.net/egg.png", 60, 101),
    SILVER(2, "초딩용", "https://d3t251u9x9cmf6.cloudfront.net/baby-dragon.png", 30, 90),
    GOLD(3, "중딩용", "https://d3t251u9x9cmf6.cloudfront.net/child-dragon.png", 20, 80),
    PLATINUM(4, "고딩용", "https://d3t251u9x9cmf6.cloudfront.net/youth-dragon.png", 0, 60);

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