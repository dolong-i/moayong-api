package com.moayong.api.domain.quiz.dto.response;

import com.moayong.api.domain.quiz.domain.Quiz;

public record QuizResponse (
        Long id,
        String financeTitle,
        String financeDescription,
        String sourceTitle,
        String sourceLink
){
    public QuizResponse(Quiz quiz) {
        this(
                quiz.getId(),
                quiz.getFinanceTitle(),
                quiz.getFinanceDescription(),
                quiz.getSourceTitle(),
                quiz.getSourceLink()
        );
    }
}

