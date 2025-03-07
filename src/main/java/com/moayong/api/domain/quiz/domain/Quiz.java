package com.moayong.api.domain.quiz.domain;

import com.moayong.api.domain.memberquiz.dto.redis.UserDailyQuiz;
import com.moayong.api.domain.quiz.converter.OptionsConverter;
import com.moayong.api.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Quiz extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "finance_title", nullable = false)
    private String financeTitle;

    @Column(name = "finance_description", nullable = false)
    private String financeDescription;

    @Column(name = "problem_title", nullable = false)
    private String problemTitle;

    @Column(name = "problem_options", nullable = false)
    @Convert(converter = OptionsConverter.class)
    public List<String> problemOptions;

    @Column(name = "answer_number", nullable = false)
    private Integer answerNumber;

    @Column(name = "answer_description", nullable = false)
    private String answerDescription;

    @Builder
    public Quiz(String financeTitle, String financeDescription, String problemTitle, List<String> problemOptions, Integer answerNumber, String answerDescription) {
        this.financeTitle = financeTitle;
        this.financeDescription = financeDescription;
        this.problemTitle = problemTitle;
        this.problemOptions = problemOptions;
        this.answerNumber = answerNumber;
        this.answerDescription = answerDescription;
    }

    public Quiz(UserDailyQuiz userDailyQuiz) {
        this.id = userDailyQuiz.getQuizId();
        this.financeTitle = userDailyQuiz.getFinanceTitle();
        this.financeDescription = userDailyQuiz.getFinanceDescription();
    }
}
