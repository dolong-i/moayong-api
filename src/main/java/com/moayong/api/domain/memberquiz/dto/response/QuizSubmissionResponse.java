package com.moayong.api.domain.memberquiz.dto.response;

import com.moayong.api.domain.memberquiz.enums.MemberQuizStatus;

public record QuizSubmissionResponse(
        Integer userAnswer,
        MemberQuizStatus status,
        Integer answerNumber,
        String answerDescription
) {
    public QuizSubmissionResponse(Integer userAnswer, QuizSubmissionDto dto){
        this(
                userAnswer,
                dto.status(),
                dto.answerNumber(),
                dto.answerDescription()
        );
    }
}
