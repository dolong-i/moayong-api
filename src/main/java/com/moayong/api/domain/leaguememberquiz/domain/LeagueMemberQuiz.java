package com.moayong.api.domain.leaguememberquiz.domain;

import com.moayong.api.domain.leaguememberquiz.enums.LeagueMemberQuizStatus;
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
@Table(name = "league_member_quiz")
public class LeagueMemberQuiz extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(name = "league_member_id", nullable = false)
    private Long leagueMemberId;

    @Column(name = "financial_quiz_id")
    private Long quizId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LeagueMemberQuizStatus status;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Builder
    public LeagueMemberQuiz(Long leagueMemberId, Long quizId, LeagueMemberQuizStatus status, Integer score) {
        this.leagueMemberId = leagueMemberId;
        this.quizId = quizId;
        this.status = status;
        this.score = score;
    }
}
