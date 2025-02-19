package com.moayong.api.global.enums;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum TierEnum {
    BRONZE(1, "브론즈", "bronze.png"),
    SILVER(2, "실버", "silver.png"),
    GOLD(3, "골드", "gold.png"),
    PLATINUM(4, "플레", "platinum.png"),
    DIAMOND(5, "다이아", "diamond.png");

    private final int id;
    private final String type;
    private final String imageUrl;
    private final int FIRST = 5;
    private final int LAST = 1;

    TierEnum(int id, String type, String imageUrl) {
        this.id = id;
        this.type = type;
        this.imageUrl = imageUrl;
    }

    public static TierEnum fromLevel(int id) {
        return Arrays.stream(values())
                .filter(tier -> tier.id == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid tier id: " + id));
    }

    public TierEnum getNextTier() {
        if (this.id == FIRST) {
            return fromLevel(this.id);
        }
        return fromLevel(this.id + 1);
    }

    public TierEnum getPrevTier() {
        if (this.id == LAST) {
            return fromLevel(this.id);
        }
        return fromLevel(this.id - 1);
    }
}