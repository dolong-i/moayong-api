package com.moayong.api.domain.memberquiz.service;

import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import com.moayong.api.domain.leaguemember.enums.LeagueMemberErrorCode;
import com.moayong.api.domain.leaguemember.exception.LeagueMemberException;
import com.moayong.api.domain.leaguemember.service.LeagueMemberService;
import com.moayong.api.domain.memberquiz.domain.MemberQuiz;
import com.moayong.api.domain.memberquiz.dto.redis.UserDailyQuiz;
import com.moayong.api.domain.memberquiz.dto.response.QuizSubmissionDto;
import com.moayong.api.domain.memberquiz.enums.MemberQuizErrorCode;
import com.moayong.api.domain.memberquiz.enums.DailyQuizRedisStatus;
import com.moayong.api.domain.memberquiz.enums.MemberQuizStatus;
import com.moayong.api.domain.memberquiz.exception.MemberQuizException;
import com.moayong.api.domain.memberquiz.repository.MemberQuizRepository;
import com.moayong.api.domain.memberquiz.repository.UserDailyQuizRedisRepository;
import com.moayong.api.domain.quiz.domain.Quiz;
import com.moayong.api.domain.quiz.service.QuizService;
import com.moayong.api.domain.user.service.UserCurrentLeagueInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberQuizService {
    private final MemberQuizRepository memberQuizRepository;
    private final QuizService quizService;
    private final UserCurrentLeagueInfoService userInfoService;
    private final LeagueMemberService memberService;
    private final UserDailyQuizRedisRepository redisRepository;

    public MemberQuiz save(MemberQuiz memberQuiz) {
        return memberQuizRepository.save(memberQuiz);
    }

    public Quiz findDailyQuiz(Long userId) {
        List<UserDailyQuiz> cachedQuizzes = redisRepository.findAllByUserId(userId);
        for (UserDailyQuiz quiz : cachedQuizzes) {
            if (quiz.getStatus().equals(DailyQuizRedisStatus.UNSOLVED.name())) {
                return new Quiz(quiz);
            }
        }

        int solvedCount = cachedQuizzes.size();

        if (solvedCount >= 5) {
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("userId", userId);
            throw new MemberQuizException(MemberQuizErrorCode.ALREADY_SOLVED_ASSIGNED_QUIZ, errorData);
        }

        Quiz randomQuiz = findRandomQuiz(userId);

        redisRepository.save(
                UserDailyQuiz.builder()
                        .id(userId + ":" + randomQuiz.getId())
                        .userId(userId)
                        .quizId(randomQuiz.getId())
                        .financeTitle(randomQuiz.getFinanceTitle())
                        .financeDescription(randomQuiz.getFinanceDescription())
                        .status(DailyQuizRedisStatus.UNSOLVED.name())
                        .ttl(getTtlUntilNext9AM().toSeconds())
                        .build());

        return randomQuiz;
    }

    private Duration getTtlUntilNext9AM() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next9AM = now.withHour(9).withMinute(0).withSecond(0).withNano(0);
        if (!now.isBefore(next9AM)) {
            next9AM = next9AM.plusDays(1);
        }

        return Duration.between(now, next9AM);
    }

    private Quiz findRandomQuiz(Long userId) {
        List<Long> solvedQuizIds = memberQuizRepository.findSolvedQuizzesByUserId(userId);

        List<Quiz> quizzesToSolve = getQuizzesToSolve(userId, solvedQuizIds);

        int randomIndex = ThreadLocalRandom.current().nextInt(quizzesToSolve.size());
        return quizzesToSolve.get(randomIndex);
    }

    private List<Quiz> getQuizzesToSolve(Long userId, List<Long> solvedQuizIds) {
        List<Quiz> quizzesToSolve;
        long countAllQuizzes = quizService.countAllQuizzes();

        if (solvedQuizIds.size() >= countAllQuizzes) {
            quizzesToSolve = quizService.findAllQuizzes();
        } else {
            quizzesToSolve = quizService.findAllQuizzesByIdNotIn(solvedQuizIds);
        }

        if (quizzesToSolve.isEmpty()) {
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("userId", userId);
            throw new MemberQuizException(MemberQuizErrorCode.EMPTY_QUIZ_TO_SOLVE, errorData);
        }
        return quizzesToSolve;
    }

    public List<Quiz> findSolvedQuizzesByUser(Long userId) {
        List<Long> solvedQuizIds = memberQuizRepository.findSolvedQuizzesByUserId(userId);

        return quizService.findAllQuizzesById(solvedQuizIds);
    }

    public List<Quiz> findSolvedQuizzesByUserAndSeason(Long userId, Long seasonId) {
        return memberQuizRepository.findSolvedQuizzesByUserAndSeason(userId, seasonId);
    }

    public QuizSubmissionDto submitAnswer(Long userId, Long quizId, Integer userAnswer) {
        Long memberId = userInfoService.findLeagueMemberId(userId);
        LeagueMember leagueMember = memberService.findById(memberId);

        Quiz quiz = findByQuizId(quizId);
        MemberQuizStatus status = quiz.getAnswerNumber().equals(userAnswer) ? MemberQuizStatus.CORRECT : MemberQuizStatus.WRONG;

        memberQuizRepository.save(
                MemberQuiz.builder()
                        .leagueMemberId(leagueMember.getId())
                        .quizId(quizId)
                        .status(status)
                        .build()
        );

        UserDailyQuiz cachedQuiz = redisRepository.findById(userId + ":" + quizId)
                .orElseThrow(() -> new RuntimeException("Redis 오류")); // 임시 Exception

        cachedQuiz.setStatus(DailyQuizRedisStatus.SOLVED.name());
        redisRepository.save(cachedQuiz);

        // TODO - TotalScore 레디스 처리

        return new QuizSubmissionDto(status, quiz);
    }

    public Quiz findByQuizId(Long quizId) {
        return quizService.findQuizById(quizId);
    }

    public Quiz findByUserAndQuiz(Long userId, Long quizId) {
        // TODO member_quiz 에 있는지 확인 필요(풀었는지 확인 필요)
        return findByQuizId(quizId);
    }
}
