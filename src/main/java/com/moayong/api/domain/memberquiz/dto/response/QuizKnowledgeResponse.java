package com.moayong.api.domain.memberquiz.dto.response;

import com.moayong.api.domain.quiz.domain.Quiz;

import java.time.LocalDateTime;

public record QuizKnowledgeResponse(
        Long id,
        String financeTitle,
        String financeDescription,
        String sourceTitle,
        String sourceLink,
        LocalDateTime createAt
) {
    public QuizKnowledgeResponse(Quiz quiz) {
        this(
                quiz.getId(),
                quiz.getFinanceTitle(),
                quiz.getFinanceDescription(),
                quiz.getSourceTitle(),
                quiz.getSourceLink(),
                quiz.getCreatedAt()
        );
    }
}
