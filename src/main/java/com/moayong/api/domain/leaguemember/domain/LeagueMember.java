package com.moayong.api.domain.leaguemember.domain;

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
@Table(name = "league_member")
public class LeagueMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name="league_id")
    private Long leagueId;

    @Column(name = "goal_amount")
    private Integer goalAmount;

    @Builder
    public LeagueMember(Long userId, Long leagueId, Integer goalAmount) {
        this.userId = userId;
        this.leagueId = leagueId;
        this.goalAmount = goalAmount;
    }
}
