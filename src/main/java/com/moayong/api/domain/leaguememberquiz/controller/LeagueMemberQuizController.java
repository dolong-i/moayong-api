package com.moayong.api.domain.leaguememberquiz.controller;

import com.moayong.api.domain.leaguememberquiz.dto.request.AnswerRequest;
import com.moayong.api.domain.leaguememberquiz.dto.response.QuizSubmissionResponse;
import com.moayong.api.domain.leaguememberquiz.service.LeagueMemberQuizService;
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
public class LeagueMemberQuizController {
    private final LeagueMemberQuizService leagueMemberQuizService;

    @GetMapping("/users/{id}/financial-quizzes/random")
    public ApiResponse<QuizResponse> getDailyQUiz (@PathVariable("id") Long userId) {
        Quiz quiz = leagueMemberQuizService.getRandomQuiz(userId);
        QuizResponse response = new QuizResponse(quiz);

        return ApiResponse.success(response, "오늘의 퀴즈 조회 성공");
    }

    @PostMapping("/users/{userId}/financial-quizzes/{quizId}/solve")
    public ApiResponse<QuizSubmissionResponse> submitQuiz(@PathVariable("userId") Long userId, @PathVariable("quizId") Long quizId, @RequestBody @Valid AnswerRequest request) {
        Integer userAnswer = request.answer();
        Quiz quiz = leagueMemberQuizService.processSubmittedAnswer(userId, quizId, userAnswer);

        QuizSubmissionResponse response = new QuizSubmissionResponse(userAnswer, quiz);
        return ApiResponse.success(response, "퀴즈 제출 성공");
    }

    @GetMapping("/users/{userId}/seasons/{seasonId}/financial-quizzes")
    public ApiResponse<List<QuizResponse>> findAllSolvedQuizzesBySeasonId(@PathVariable("userId") Long userId, @PathVariable("seasonId") Long seasonId) {
        List<Quiz> quizzes = leagueMemberQuizService.findSolvedQuizzesByUserAndSeason(userId, seasonId);
        List<QuizResponse> response = quizzes.stream().map(QuizResponse::new).toList();
        return ApiResponse.success(response, "현재 시즌동안 완료한 퀴즈 조회 성공");
    }

    @GetMapping("/users/{id}/financial-quizzes")
    public ApiResponse<List<QuizResponse>> findAllSolvedQuizzesByUserId(@PathVariable("id") Long userId) {
        List<Quiz> quizzes = leagueMemberQuizService.findAllSolvedQuizzes(userId);
        List<QuizResponse> response = quizzes.stream().map(QuizResponse::new).toList();

        return ApiResponse.success(response, "완료한 퀴즈 전체 조회 성공");
    }
}
