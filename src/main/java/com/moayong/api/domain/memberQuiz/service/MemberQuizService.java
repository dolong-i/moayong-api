package com.moayong.api.domain.memberQuiz.service;

import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import com.moayong.api.domain.leaguemember.enums.LeagueMemberErrorCode;
import com.moayong.api.domain.leaguemember.exception.LeagueMemberException;
import com.moayong.api.domain.leaguemember.service.LeagueMemberService;
import com.moayong.api.domain.memberQuiz.domain.MemberQuiz;
import com.moayong.api.domain.memberQuiz.dto.response.DailyQuizResponse;
import com.moayong.api.domain.memberQuiz.dto.response.QuizSubmissionResponse;
import com.moayong.api.domain.memberQuiz.dto.response.SolvedQuizResponse;
import com.moayong.api.domain.memberQuiz.enums.MemberQuizStatus;
import com.moayong.api.domain.memberQuiz.repository.MemberQuizRepository;
import com.moayong.api.domain.quiz.domain.Quiz;
import com.moayong.api.domain.quiz.dto.response.QuizResponse;
import com.moayong.api.domain.quiz.enums.QuizErrorCode;
import com.moayong.api.domain.quiz.exception.QuizException;
import com.moayong.api.domain.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberQuizService {
    private final MemberQuizRepository memberQuizRepository;
    private final QuizService quizService;
    private final LeagueMemberService leagueMemberService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Quiz> quizRedisTemplate;

    public MemberQuiz save(MemberQuiz memberQuiz) {
        return memberQuizRepository.save(memberQuiz);
    }

    public DailyQuizResponse findDailyQuiz(Long userId) {
        LocalDate criteriaDate = getCriteriaDate();
        String dailyQuizKey = getDailyQuizKey(userId, criteriaDate);

        if (quizRedisTemplate.hasKey(dailyQuizKey)) {
            Quiz cachedQuiz = quizRedisTemplate.opsForValue().get(dailyQuizKey);
            return new DailyQuizResponse(false, new QuizResponse(cachedQuiz), null);
        }

        Long solvedCount = 0L;
        String solvedCountKey = getSolvedCountKey(userId, criteriaDate);
        if (redisTemplate.hasKey(solvedCountKey)) {
            solvedCount = (Long) redisTemplate.opsForValue().get(solvedCountKey);
        }

        if (solvedCount >= 5) {
            List<Long> solvedQuizIds = memberQuizRepository.findSolvedQuizzesByUserId(userId);
            List<SolvedQuizResponse> solvedQuizResponses = quizService.findAllQuizzesById(solvedQuizIds).stream().map(SolvedQuizResponse::new).toList();
            return new DailyQuizResponse(true, null, solvedQuizResponses);
        }

        Quiz randomQuiz = findRandomQuiz(userId);
        quizRedisTemplate.opsForValue().set(dailyQuizKey, randomQuiz, getTtlUntilNext9AM());

        return new DailyQuizResponse(false, new QuizResponse(randomQuiz), null);
    }

    private String getSolvedCountKey(Long userId, LocalDate criteriaDate) {
        return "dailySolvedCount:" + userId + ":" + criteriaDate;
    }

    private String getDailyQuizKey(Long userId, LocalDate criteriaDate) {
        return "dailyQuiz:" + userId + ":" + criteriaDate;
    }

    private Duration getTtlUntilNext9AM() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next9AM = now.withHour(9).withMinute(0).withSecond(0).withNano(0);
        if (!now.isBefore(next9AM)) {
            next9AM = next9AM.plusDays(1);
        }

        return Duration.between(now, next9AM);
    } 

    // 오전 9시 기준
    private LocalDate getCriteriaDate() {
        return LocalTime.now().isBefore(LocalTime.of(9, 0)) ? LocalDate.now().minusDays(1) : LocalDate.now();
    }

    private Quiz findRandomQuiz(Long userId) {
        List<Long> solvedQuizIds = memberQuizRepository.findSolvedQuizzesByUserId(userId);

        List<Quiz> quizzes;
        long totalQuizzes = quizService.countAllQuizzes();

        if (solvedQuizIds.size() >= totalQuizzes) {
            quizzes = quizService.findAllQuizzes();
        } else {
            quizzes = quizService.findAllQuizzesByIdNotIn(solvedQuizIds);
        }

        if (quizzes.isEmpty()) {
            throw new QuizException(QuizErrorCode.EMPTY_QUIZ_LIST);
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(quizzes.size());
        return quizzes.get(randomIndex);
    }

    public List<Quiz> findSolvedQuizzesByUser(Long userId) {
        List<Long> solvedQuizIds = memberQuizRepository.findSolvedQuizzesByUserId(userId);

        return quizService.findAllQuizzesById(solvedQuizIds);
    }

    public List<Quiz> findSolvedQuizzesByUserAndSeason(Long userId, Long seasonId) {
        return memberQuizRepository.findSolvedQuizzesByUserAndSeason(userId, seasonId);
    }

    public QuizSubmissionResponse submitAnswer(Long userId, Long quizId, Integer userAnswer) {
        LeagueMember leagueMember = findCurrentLeagueMember(userId);

        Quiz quiz = findByQuizId(quizId);
        MemberQuizStatus status = quiz.getAnswerNumber().equals(userAnswer) ? MemberQuizStatus.SUCCESS : MemberQuizStatus.FAIL;

        memberQuizRepository.save(
                MemberQuiz.builder()
                        .leagueMemberId(leagueMember.getId())
                        .quizId(quizId)
                        .status(status)
                        .build()
        );

        LocalDate criteriaDate = getCriteriaDate();

        String dailyQuizKey = getDailyQuizKey(userId, criteriaDate);
        quizRedisTemplate.delete(dailyQuizKey);

        String solvedCountKey = getSolvedCountKey(userId, criteriaDate);
        if (!redisTemplate.hasKey(solvedCountKey)) {
            redisTemplate.opsForValue().set(solvedCountKey, 0L, getTtlUntilNext9AM());
        }

        redisTemplate.opsForValue().increment(solvedCountKey, 1L);

        // TODO - TotalScore 레디스 처리

        return new QuizSubmissionResponse(userAnswer, status, quiz);
    }

    private LeagueMember findCurrentLeagueMember(Long userId) {
        return leagueMemberService.findCurrentLeagueMemberOptional(userId)
                .orElseThrow(() -> {
                    Map<String, Object> errorData = new HashMap<>();
                    errorData.put("userId", userId);
                    return new LeagueMemberException(LeagueMemberErrorCode.LEAGUE_MEMBER_NOT_FOUND, errorData);
                });
    }

    public Quiz findByQuizId(Long quizId) {
        return quizService.findQuizByIdOptional(quizId)
                .orElseThrow(() -> {
                    Map<String, Object> errorData = new HashMap<>();
                    errorData.put("quizId", quizId);
                    return new QuizException(QuizErrorCode.QUIZ_NOT_FOUND, errorData);
                });
    }
}
