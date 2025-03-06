package com.moayong.api.domain.quiz.dto.response;

import com.moayong.api.domain.quiz.domain.Quiz;

public record QuizResponse (
        Long id,
        String financeTopic,
        String financeInfo
){
    public QuizResponse(Quiz quiz) {
        this(
                quiz.getId(),
                quiz.getFinanceTopic(),
                quiz.getFinanceInfo()
        );
    }
}

