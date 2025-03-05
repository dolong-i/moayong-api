package com.moayong.api.domain.memberQuiz.controller;

import com.moayong.api.domain.memberQuiz.dto.request.AnswerRequest;
import com.moayong.api.domain.memberQuiz.dto.response.QuizSubmissionResponse;
import com.moayong.api.domain.memberQuiz.service.MemberQuizService;
import com.moayong.api.domain.quiz.domain.Quiz;
import com.moayong.api.domain.quiz.dto.response.QuizResponse;
import com.moayong.api.global.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberQuizController {
    private final MemberQuizService memberQuizService;

    @GetMapping("/users/{id}/quizzes/random")
    public ApiResponse<QuizResponse> getDailyQUiz (@PathVariable("id") Long userId) {
        Quiz quiz = memberQuizService.findRandomQuiz(userId);
        QuizResponse response = new QuizResponse(quiz);

        return ApiResponse.success(response, "오늘의 퀴즈 조회 성공");
    }

    @PostMapping("/users/{userId}/quizzes/{quizId}/solve")
    public ApiResponse<QuizSubmissionResponse> submitQuiz(@PathVariable("userId") Long userId, @PathVariable("quizId") Long quizId, @RequestBody @Valid AnswerRequest request) {
        Integer userAnswer = request.answer();
        Quiz quiz = memberQuizService.submitAnswer(userId, quizId, userAnswer);

        QuizSubmissionResponse response = new QuizSubmissionResponse(userAnswer, quiz);
        return ApiResponse.success(response, "퀴즈 제출 성공");
    }

    @GetMapping("/users/{userId}/seasons/{seasonId}/quizzes")
    public ApiResponse<List<QuizResponse>> findAllSolvedQuizzesBySeasonId(@PathVariable("userId") Long userId, @PathVariable("seasonId") Long seasonId) {
        List<Quiz> quizzes = memberQuizService.findSolvedQuizzesByUserAndSeason(userId, seasonId);
        List<QuizResponse> response = quizzes.stream().map(QuizResponse::new).toList();
        return ApiResponse.success(response, "현재 시즌동안 완료한 퀴즈 조회 성공");
    }

    @GetMapping("/users/{id}/quizzes")
    public ApiResponse<List<QuizResponse>> findAllSolvedQuizzesByUserId(@PathVariable("id") Long userId) {
        List<Quiz> quizzes = memberQuizService.findAllSolvedQuizzes(userId);
        List<QuizResponse> response = quizzes.stream().map(QuizResponse::new).toList();

        return ApiResponse.success(response, "완료한 퀴즈 전체 조회 성공");
    }
}
