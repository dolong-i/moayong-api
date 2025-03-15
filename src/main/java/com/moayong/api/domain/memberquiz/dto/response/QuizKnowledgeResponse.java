package com.moayong.api.domain.memberquiz.dto.response;

import com.moayong.api.domain.quiz.domain.Quiz;

public record QuizKnowledgeResponse(
        Long id,
        String financeTitle,
        String financeDescription,
        String sourceTitle,
        String sourceLink
) {
    public QuizKnowledgeResponse(Quiz quiz) {
        this(
                quiz.getId(),
                quiz.getFinanceTitle(),
                quiz.getFinanceDescription(),
                quiz.getSourceTitle(),
                quiz.getSourceLink()
        );
    }
}
