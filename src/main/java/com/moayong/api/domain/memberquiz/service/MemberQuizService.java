package com.moayong.api.domain.memberquiz.service;

import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import com.moayong.api.domain.leaguemember.service.LeagueMemberService;
import com.moayong.api.domain.memberquiz.domain.MemberQuiz;
import com.moayong.api.domain.memberquiz.domain.UserDailyQuiz;
import com.moayong.api.domain.memberquiz.dto.service.QuizSubmissionDto;
import com.moayong.api.domain.memberquiz.enums.DailyQuizRedisStatus;
import com.moayong.api.domain.memberquiz.enums.MemberQuizErrorCode;
import com.moayong.api.domain.memberquiz.enums.MemberQuizStatus;
import com.moayong.api.domain.memberquiz.exception.MemberQuizException;
import com.moayong.api.domain.memberquiz.repository.MemberQuizRepository;
import com.moayong.api.domain.quiz.domain.Quiz;
import com.moayong.api.domain.quiz.service.QuizService;
import com.moayong.api.domain.user.service.UserCurrentLeagueInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberQuizService {
    private final MemberQuizRepository memberQuizRepository;
    private final QuizService quizService;
    private final UserCurrentLeagueInfoService userInfoService;
    private final LeagueMemberService memberService;
    private final UserDailyQuizService dailyQuizService;

    public MemberQuiz save(MemberQuiz memberQuiz) {
        return memberQuizRepository.save(memberQuiz);
    }

    public Quiz findDailyQuiz(Long userId) {
        List<UserDailyQuiz> cachedQuizzes = dailyQuizService.findAllByUserId(userId);
        for (UserDailyQuiz quiz : cachedQuizzes) {
            if (quiz.getStatus().equals(DailyQuizRedisStatus.UNSOLVED.name())) {
                return quizService.findQuizById(quiz.getQuizId());
            }
        }

        int solvedCount = cachedQuizzes.size();
        if (solvedCount >= 5) {
            throw new MemberQuizException(MemberQuizErrorCode.ALREADY_SOLVED_ASSIGNED_QUIZ);
        }

        Quiz randomQuiz = findRandomQuiz(userId);

        dailyQuizService.save(userId, randomQuiz);

        return randomQuiz;
    }


    private Quiz findRandomQuiz(Long userId) {
        List<Long> solvedQuizIds = memberQuizRepository.findSolvedQuizzesByUserId(userId);

        List<Quiz> quizzesToSolve = getQuizzesToSolve(solvedQuizIds);

        int randomIndex = ThreadLocalRandom.current().nextInt(quizzesToSolve.size());
        return quizzesToSolve.get(randomIndex);
    }

    private List<Quiz> getQuizzesToSolve(List<Long> solvedQuizIds) {
        List<Quiz> quizzesToSolve;
        long countAllQuizzes = quizService.countAllQuizzes();

        if (solvedQuizIds.size() >= countAllQuizzes) {
            quizzesToSolve = quizService.findAllQuizzes();
        } else {
            quizzesToSolve = quizService.findAllQuizzesByIdNotIn(solvedQuizIds);
        }

        if (quizzesToSolve.isEmpty()) {
            throw new MemberQuizException(MemberQuizErrorCode.EMPTY_QUIZ_TO_SOLVE);
        }
        return quizzesToSolve;
    }

    public List<Quiz> findSolvedQuizzesByUser(Long userId) {
        List<Long> solvedQuizIds = memberQuizRepository.findSolvedQuizzesByUserId(userId);

        return quizService.findAllQuizzesById(solvedQuizIds);
    }

    public List<Quiz> findSolvedQuizzesByUserAndSeason(Long userId, Long seasonId) {
        // 유저가 해당 시즌동안 푼 퀴즈
        return memberQuizRepository.findSolvedQuizzesByUserAndSeason(userId, seasonId);
    }

    @Transactional
    public QuizSubmissionDto submitAnswer(Long userId, Long quizId, Integer userAnswer) {
        if (memberQuizRepository.findSolvedQuizzesByUserId(userId).stream().anyMatch(id -> id.equals(quizId))) {
            throw new MemberQuizException(MemberQuizErrorCode.ALREADY_SUBMITTED);
        }

        UserDailyQuiz cachedQuiz = checkQuizInProgress(userId, quizId);

        Long memberId = userInfoService.findLeagueMemberId(userId);
        LeagueMember leagueMember = memberService.findById(memberId);

        Quiz quiz = quizService.findQuizById(quizId);
        MemberQuizStatus status = quiz.getAnswerNumber().equals(userAnswer) ? MemberQuizStatus.CORRECT : MemberQuizStatus.WRONG;

        MemberQuiz savedMemberQuiz = memberQuizRepository.save(
                MemberQuiz.builder()
                        .leagueMemberId(leagueMember.getId())
                        .quizId(quizId)
                        .status(status)
                        .build()
        );

        dailyQuizService.updateStatus(cachedQuiz, DailyQuizRedisStatus.SOLVED);
        leagueMember.addScore(savedMemberQuiz.getScore());

        return new QuizSubmissionDto(status, quiz);
    }

    // 풀고있는 퀴즈인지 확인
    private UserDailyQuiz checkQuizInProgress(Long userId, Long quizId) {
        return dailyQuizService.findByIdOptional(userId, quizId)
                .orElseThrow(() -> new MemberQuizException(MemberQuizErrorCode.QUIZ_NOT_IN_PROGRESS));
    }

    public Quiz startQuizChallenge(Long userId, Long quizId) {
        checkQuizInProgress(userId, quizId);
        return quizService.findQuizById(quizId);
    }


    public Quiz findSolvedQuiz(Long userId, Long quizId) {
        if (memberQuizRepository.findSolvedQuizzesByUserId(userId).stream().noneMatch(id -> id.equals(quizId))) {
            throw new MemberQuizException(MemberQuizErrorCode.QUIZ_NOT_SOLVED);
        }

        return quizService.findQuizById(quizId);
    }
}
