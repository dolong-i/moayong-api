package com.moayong.api.domain.quiz.controller;

import com.moayong.api.domain.leaguememberquiz.service.LeagueMemberQuizService;
import com.moayong.api.domain.quiz.domain.Quiz;
import com.moayong.api.domain.quiz.dto.response.QuizResponse;
import com.moayong.api.domain.quiz.service.QuizService;
import com.moayong.api.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class QuizController {
    private final QuizService quizService;
    private final LeagueMemberQuizService leagueMemberQuizService;

    @GetMapping("/users/{id}/missions/financial-quizzes/today")
    public ApiResponse<QuizResponse> getDailyQUiz (@PathVariable("id") Long userId) {
        Quiz quiz = quizService.getRandomQuiz(userId);
        QuizResponse response = new QuizResponse(quiz);

        return ApiResponse.success(response, "오늘의 퀴즈 조회 성공");
    }

    // TODO 퀴즈 제출
    @PostMapping("/users/{userId}/financial-quizzes/{quizId}/solve")
    public ApiResponse<QuizResponse> submitQuiz(@PathVariable("userId") Long userId, @PathVariable("quizId") Long quizId, @RequestParam Integer userAnswer) {
        leagueMemberQuizService.createLeagueMemberQuiz(userId, quizId, userAnswer);
        Quiz quiz = quizService.findById(quizId);
        QuizResponse quizResponse = new QuizResponse(quiz);

        return ApiResponse.success(quizResponse, "퀴즈 제출 성공");
    }

    // TODO 지난 퀴즈들 보기
    @GetMapping("/users/{id}/financial-quizzes")
    public ApiResponse<List<QuizResponse>> findAll(@PathVariable("id") Long userId) {
//        List<Season> seasons = seasonService.findAllSeasons();
        List<Quiz> quizzes = quizService.findAllSolvedQuizzes(userId);
        List<QuizResponse> response = quizzes.stream().map(QuizResponse::new).toList();

        return ApiResponse.success(response, "퀴즈 전체 조회 성공");
    }

    @GetMapping("/missions/financial-quizzes/{id}")
    public ApiResponse<QuizResponse> getQuiz(@PathVariable("id") Long id) {
        Quiz quiz = quizService.findById(id);
        QuizResponse quizResponse = new QuizResponse(quiz);

        return ApiResponse.success(quizResponse, "퀴즈 단건 조회 성공");
    }
}
