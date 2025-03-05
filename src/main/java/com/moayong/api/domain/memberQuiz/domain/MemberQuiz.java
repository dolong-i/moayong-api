package com.moayong.api.domain.memberQuiz.domain;

import com.moayong.api.domain.memberQuiz.enums.MemberQuizStatus;
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
@Table(name = "member_quiz")
public class MemberQuiz extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(name = "league_member_id", nullable = false)
    private Long leagueMemberId;

    @Column(name = "quiz_id")
    private Long quizId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberQuizStatus status;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Builder
    public MemberQuiz(Long leagueMemberId, Long quizId, MemberQuizStatus status) {
        this.leagueMemberId = leagueMemberId;
        this.quizId = quizId;
        this.status = status;
        this.score = status.getScore();
    }
}
