package com.moayong.api.domain.memberquiz.controller;

import com.moayong.api.domain.memberquiz.dto.request.AnswerRequest;
import com.moayong.api.domain.memberquiz.dto.response.QuizKnowledgeResponse;
import com.moayong.api.domain.memberquiz.dto.response.QuizProblemResponse;
import com.moayong.api.domain.memberquiz.dto.response.QuizSubmissionResponse;
import com.moayong.api.domain.memberquiz.dto.service.QuizSubmissionDto;
import com.moayong.api.domain.memberquiz.service.MemberQuizService;
import com.moayong.api.domain.quiz.domain.Quiz;
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
    public ApiResponse<QuizKnowledgeResponse> findDailyQuiz(@PathVariable("id") Long userId) {
        Quiz quiz = memberQuizService.findDailyQuiz(userId);
        QuizKnowledgeResponse response = new QuizKnowledgeResponse(quiz);

        return ApiResponse.success(response, "오늘의 퀴즈 조회 성공");
    }

    @GetMapping("/users/{userId}/quizzes/{quizId}/problem")
    public ApiResponse<QuizProblemResponse> showQuiz(@PathVariable("userId") Long userId, @PathVariable("quizId") Long quizId) {
        Quiz quiz = memberQuizService.startQuizChallenge(userId, quizId);
        QuizProblemResponse response = new QuizProblemResponse(quiz);
        return ApiResponse.success(response, "퀴즈 도전하기 진입 성공");
    }

    @PostMapping("/users/{userId}/quizzes/{quizId}/solve")
    public ApiResponse<QuizSubmissionResponse> submitQuiz(@PathVariable("userId") Long userId, @PathVariable("quizId") Long quizId, @Valid @RequestBody AnswerRequest request) {
        Integer userAnswer = request.answer();
        QuizSubmissionDto dto = memberQuizService.submitAnswer(userId, quizId, userAnswer);
        QuizSubmissionResponse response = new QuizSubmissionResponse(userAnswer, dto);

        return ApiResponse.success(response, "퀴즈 제출 성공");
    }

    @GetMapping("/users/{userId}/seasons/{seasonId}/quizzes")
    public ApiResponse<List<QuizKnowledgeResponse>> findAllSolvedQuizzesBySeasonId(@PathVariable("userId") Long userId, @PathVariable("seasonId") Long seasonId) {
        List<Quiz> quizzes = memberQuizService.findSolvedQuizzesByUserAndSeason(userId, seasonId);
        List<QuizKnowledgeResponse> response = quizzes.stream().map(QuizKnowledgeResponse::new).toList();
        return ApiResponse.success(response, "현재 시즌동안 완료한 퀴즈 조회 성공");
    }

    @GetMapping("/users/{id}/quizzes")
    public ApiResponse<List<QuizKnowledgeResponse>> findAllSolvedQuizzesByUserId(@PathVariable("id") Long userId) {
        List<Quiz> quizzes = memberQuizService.findSolvedQuizzesByUser(userId);
        List<QuizKnowledgeResponse> response = quizzes.stream().map(QuizKnowledgeResponse::new).toList();

        return ApiResponse.success(response, "완료한 퀴즈 전체 조회 성공");
    }

    @GetMapping("/users/{userId}/quizzes/{quizId}")
    public ApiResponse<QuizKnowledgeResponse> findSolvedQuiz(@PathVariable("userId") Long userId, @PathVariable("quizId") Long quizId) {
        Quiz quiz = memberQuizService.findSolvedQuiz(userId, quizId);
        QuizKnowledgeResponse response = new QuizKnowledgeResponse(quiz);

        return ApiResponse.success(response, "완료한 퀴즈 단건 조회 성공");
    }
}
