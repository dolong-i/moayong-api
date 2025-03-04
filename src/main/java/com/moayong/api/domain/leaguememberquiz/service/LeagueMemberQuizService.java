package com.moayong.api.domain.leaguememberquiz.service;

import com.moayong.api.domain.league.service.LeagueService;
import com.moayong.api.domain.leaguememberquiz.domain.LeagueMemberQuiz;
import com.moayong.api.domain.leaguememberquiz.repository.LeagueMemberQuizRepository;
import com.moayong.api.domain.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LeagueMemberQuizService {
    private final LeagueMemberQuizRepository leagueMemberQuizRepository;

    public LeagueMemberQuiz save(LeagueMemberQuiz leagueMemberQuiz) {
        return leagueMemberQuizRepository.save(leagueMemberQuiz);
    }

    public void createLeagueMemberQuiz(Long userId, Long quizId, Integer userAnswer) {
        // TODO
    }
}
