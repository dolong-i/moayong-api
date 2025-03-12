package com.moayong.api.domain.league.domain;

import com.moayong.api.domain.league.enums.Tier;
import com.moayong.api.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "League")
public class League extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "season_id", updatable = false)
    private Long seasonId;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "promotion_rate", nullable = false)
    private Integer promotionRate;

    @Column(name = "relegation_rate", nullable = false)
    private Integer relegationRate;

    @Builder
    public League(Long seasonId, Tier tier) {
        this.seasonId = seasonId;
        this.level = tier.getLevel();
        this.name = tier.getName();
        this.imageUrl = tier.getImageUrl();
        this.promotionRate = tier.getPromotionRate();
        this.relegationRate = tier.getRelegationRate();
    }
}
