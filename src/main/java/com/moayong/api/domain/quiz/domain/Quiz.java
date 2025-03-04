package com.moayong.api.domain.quiz.domain;

import com.moayong.api.domain.quiz.converter.OptionsConverter;
import com.moayong.api.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "financial_quiz")
public class Quiz extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "options", nullable = false)
    @Convert(converter = OptionsConverter.class)
    public List<String> options;

    @Column(name = "answer_number", nullable = false)
    private Integer answerNumber;

    @Column(name = "answer_title", nullable = false)
    private String answerTitle;

    @Column(name = "answer_description", nullable = false)
    private String answerDescription;

    @Builder
    public Quiz(String title, String description, List<String> options, Integer answerNumber, String answerTitle, String answerDescription) {
        this.title = title;
        this.description = description;
        this.options = options;
        this.answerNumber = answerNumber;
        this.answerTitle = answerTitle;
        this.answerDescription = answerDescription;
    }
}
