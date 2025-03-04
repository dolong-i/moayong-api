package com.moayong.api.domain.quiz.dto.response;

import com.moayong.api.domain.quiz.domain.Quiz;

import java.util.List;

public record QuizResponse (
        Long id,
        String title,
        String description,
        List<String> options
){
    public QuizResponse(Quiz quiz) {
        this(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getDescription(),
                quiz.getOptions()
        );
    }
}

