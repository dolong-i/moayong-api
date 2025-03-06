package com.moayong.api.domain.memberQuiz.dto.response;

import com.moayong.api.domain.memberQuiz.enums.MemberQuizStatus;
import com.moayong.api.domain.quiz.domain.Quiz;

public record QuizSubmissionResponse(
        Integer userAnswer,
        MemberQuizStatus status,
        Integer answerNumber,
        String description
) {
    public QuizSubmissionResponse(Integer userAnswer, MemberQuizStatus status, Quiz quiz){
        this(
                userAnswer,
                status,
                quiz.getAnswerNumber(),
                quiz.getDescription()
        );
    }
}
