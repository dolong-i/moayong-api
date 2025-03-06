package com.moayong.api.domain.memberQuiz.dto.response;

import com.moayong.api.domain.quiz.domain.Quiz;

import java.util.List;

public record QuizViewResponse(
        Long id,
        String title,
        List<String> options
) {
    public QuizViewResponse(Quiz quiz) {
        this(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getOptions()
        );
    }
}
