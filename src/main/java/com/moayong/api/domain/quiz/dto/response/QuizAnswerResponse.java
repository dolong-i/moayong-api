package com.moayong.api.domain.quiz.dto.response;

import com.moayong.api.domain.quiz.domain.Quiz;

public record QuizAnswerResponse (
        Long id,
        Integer answerNumber,
        String answerTitle,
        String answerDescription
) {
    public QuizAnswerResponse(Quiz quiz){
        this(
                quiz.getId(),
                quiz.getAnswerNumber(),
                quiz.getAnswerTitle(),
                quiz.getAnswerDescription()
        );
    }
}
