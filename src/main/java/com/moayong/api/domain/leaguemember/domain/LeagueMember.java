package com.moayong.api.domain.leaguemember.domain;

import com.moayong.api.domain.leaguemember.enums.LeagueMemberStatus;
import com.moayong.api.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
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

    @Column(name="total_score")
    private Integer totalScore;

    @Column(name = "goal_amount")
    private Integer goalAmount;

    @Setter
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LeagueMemberStatus status;

    @Builder
    public LeagueMember(Long userId, Long leagueId, LeagueMemberStatus status, Integer goalAmount, Integer totalScore) {
        this.userId = userId;
        this.leagueId = leagueId;
        this.status = status;
        this.goalAmount = goalAmount;
        this.totalScore = totalScore;
    }

    public void addScore(int score) {
        this.totalScore += score;
    }
}
