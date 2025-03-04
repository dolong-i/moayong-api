package com.moayong.api.domain.leaguememberquiz.dto.response;

import com.moayong.api.domain.quiz.domain.Quiz;

public record QuizSubmissionResponse(
        Integer userAnswer,
        String title,
        Integer answerNumber,
        String answerTitle,
        String answerDescription
) {
    public QuizSubmissionResponse(Integer userAnswer, Quiz quiz){
        this(
                userAnswer,
                quiz.getTitle(),
                quiz.getAnswerNumber(),
                quiz.getAnswerTitle(),
                quiz.getAnswerDescription()
        );
    }
}
