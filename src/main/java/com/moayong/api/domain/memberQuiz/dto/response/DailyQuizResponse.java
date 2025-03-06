package com.moayong.api.domain.memberQuiz.dto.response;

import com.moayong.api.domain.quiz.dto.response.QuizResponse;

import java.util.List;

public record DailyQuizResponse(
        boolean solveFiveQuizzes,
        QuizResponse quizResponse,
        List<SolvedQuizResponse> solvedQuizResponses
) {
}
