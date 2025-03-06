package com.moayong.api.domain.quiz.domain;

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
public class Quiz extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "finance_topic", nullable = false)
    private String financeTopic;

    @Column(name = "finance_info", nullable = false)
    private String financeInfo;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "options", nullable = false)
    @Convert(converter = OptionsConverter.class)
    public List<String> options;

    @Column(name = "answer_number", nullable = false)
    private Integer answerNumber;

    @Builder
    public Quiz(String financeTopic, String financeInfo, String title, String description, List<String> options, Integer answerNumber) {
        this.financeTopic = financeTopic;
        this.financeInfo = financeInfo;
        this.title = title;
        this.description = description;
        this.options = options;
        this.answerNumber = answerNumber;
    }
}
