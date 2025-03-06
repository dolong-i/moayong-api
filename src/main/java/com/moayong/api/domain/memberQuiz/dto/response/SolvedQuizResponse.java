package com.moayong.api.domain.memberQuiz.dto.response;

import com.moayong.api.domain.quiz.domain.Quiz;

public record SolvedQuizResponse(
        Long quizId,
        String financialTopic
) {
    public SolvedQuizResponse(Quiz quiz) {
        this(
                quiz.getId(),
                quiz.getFinanceTopic()
        );
    }
}
