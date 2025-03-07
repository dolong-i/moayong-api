package com.moayong.api.domain.memberquiz.dto.response;

import com.moayong.api.domain.quiz.domain.Quiz;

import java.util.List;

public record QuizProblemResponse(
        Long id,
        String problemTitle,
        List<String> problemOptions
) {
    public QuizProblemResponse(Quiz quiz) {
        this(
                quiz.getId(),
                quiz.getProblemTitle(),
                quiz.getProblemOptions()
        );
    }
}
