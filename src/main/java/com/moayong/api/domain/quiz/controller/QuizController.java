package com.moayong.api.domain.quiz.controller;

import com.moayong.api.domain.quiz.domain.Quiz;
import com.moayong.api.domain.quiz.dto.response.QuizResponse;
import com.moayong.api.domain.quiz.service.QuizService;
import com.moayong.api.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class QuizController {
    private final QuizService quizService;

    @GetMapping("/quizzes/{id}")
    public ApiResponse<QuizResponse> findByQuizId(@PathVariable("id") Long id) {
        Quiz quiz = quizService.findQuizById(id);
        QuizResponse quizResponse = new QuizResponse(quiz);

        return ApiResponse.success(quizResponse, "퀴즈 단건 조회 성공");
    }

    @PostMapping("/quizzes")
    public ApiResponse<Void> save() {
        quizService.saveQuizzes();

        return ApiResponse.success(null, "퀴즈 생성 성공");
    }
}