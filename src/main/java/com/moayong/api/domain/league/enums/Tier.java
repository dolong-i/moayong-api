package com.moayong.api.domain.league.enums;

import lombok.Getter;

@Getter
public enum Tier {
    EGG(1, "알이용", "https://d3t251u9x9cmf6.cloudfront.net/egg-dragon.png", 60, 101),
    BABY(2, "아기용", "https://d3t251u9x9cmf6.cloudfront.net/baby-dragon.png", 30, 90),
    CHILD(3, "어린이용", "https://d3t251u9x9cmf6.cloudfront.net/child-dragon.png", 20, 80),
    TEEN(4, "청소년용", "https://d3t251u9x9cmf6.cloudfront.net/young-dragon.png", 10, 60),
    ADULT(5, "어른용", "https://d3t251u9x9cmf6.cloudfront.net/adult-dragon.png", 10, 60),
    GOD(6, "짱이용", "https://d3t251u9x9cmf6.cloudfront.net/god-dragon.png", 0, 60);

    private final int level;
    private final String name;
    private final String imageUrl;
    private final int PromotionRate;
    private final int RelegationRate;

    Tier(int level, String name, String imageUrl, int PromotionRate, int RelegationRate) {
        this.level = level;
        this.name = name;
        this.imageUrl = imageUrl;
        this.PromotionRate = PromotionRate;
        this.RelegationRate = RelegationRate;
    }
}