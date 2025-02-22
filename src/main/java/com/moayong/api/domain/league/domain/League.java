package com.moayong.api.domain.league.domain;

import com.moayong.api.domain.season.domain.Season;
import com.moayong.api.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
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

    @ManyToOne
    @JoinColumn(name="season_id")
    private Season season;

    @Column(name = "tier_id", nullable = false)
    private Integer tierId;

    @Builder
    public League(Integer tierId, Season season) {
        this.tierId = tierId;
        this.season = season;
    }
}
