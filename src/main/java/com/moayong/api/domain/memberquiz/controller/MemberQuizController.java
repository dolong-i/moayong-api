package com.moayong.api.domain.memberquiz.controller;

import com.moayong.api.domain.auth.config.UserPrincipal;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberQuizController {
    private final MemberQuizService memberQuizService;

    @GetMapping("/members/{memberId}/quizzes/random")
    public ApiResponse<QuizKnowledgeResponse> findDailyQuiz(@AuthenticationPrincipal UserPrincipal principal, @PathVariable("memberId") Long memberId) {
        Quiz quiz = memberQuizService.findDailyQuiz(Long.valueOf(principal.getUserId()), memberId);
        QuizKnowledgeResponse response = new QuizKnowledgeResponse(quiz);

        return ApiResponse.success(response, "오늘의 퀴즈 조회 성공");
    }

    @GetMapping("/members/{memberId}/quizzes/{quizId}/problem")
    public ApiResponse<QuizProblemResponse> showQuiz(@PathVariable("memberId") Long memberId, @PathVariable("quizId") Long quizId) {
        Quiz quiz = memberQuizService.startQuizChallenge(memberId, quizId);
        QuizProblemResponse response = new QuizProblemResponse(quiz);
        return ApiResponse.success(response, "퀴즈 도전하기 진입 성공");
    }

    @PostMapping("/members/{memberId}/quizzes/{quizId}/solve")
    public ApiResponse<QuizSubmissionResponse> submitQuiz(@AuthenticationPrincipal UserPrincipal principal, @PathVariable("memberId") Long memberId, @PathVariable("quizId") Long quizId, @Valid @RequestBody AnswerRequest request) {
        Integer userAnswer = request.answer();
        QuizSubmissionDto dto = memberQuizService.submitAnswer(Long.valueOf(principal.getUserId()), memberId, quizId, userAnswer);
        QuizSubmissionResponse response = new QuizSubmissionResponse(userAnswer, dto);

        return ApiResponse.success(response, "퀴즈 제출 성공");
    }

    @GetMapping("/users/{userId}/quizzes")
    public ApiResponse<List<QuizKnowledgeResponse>> findAllSolvedQuizzesByUserId(@PathVariable("userId") Long userId) {
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
