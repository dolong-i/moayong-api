package com.moayong.api.domain.memberquiz.dto.response;

import com.moayong.api.domain.memberquiz.enums.MemberQuizStatus;
import com.moayong.api.domain.quiz.domain.Quiz;

public record QuizSubmissionDto(
        MemberQuizStatus status,
        Integer answerNumber,
        String answerDescription
) {
    public QuizSubmissionDto(MemberQuizStatus status, Quiz quiz){
        this(
                status,
                quiz.getAnswerNumber(),
                quiz.getAnswerDescription()
        );
    }
}
