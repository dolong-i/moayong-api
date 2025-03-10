package com.moayong.api.domain.memberquiz.service;

import com.moayong.api.domain.memberquiz.domain.UserDailyQuiz;
import com.moayong.api.domain.memberquiz.enums.DailyQuizRedisStatus;
import com.moayong.api.domain.memberquiz.repository.UserDailyQuizRepository;
import com.moayong.api.domain.quiz.domain.Quiz;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDailyQuizService {
    private final UserDailyQuizRepository dailyQuizRepository;

    public List<UserDailyQuiz> findAllByUserId(Long userId) {
        return dailyQuizRepository.findAllByUserId(userId);
    }

    public void save(Long userId, Quiz quiz) {
        dailyQuizRepository.save(UserDailyQuiz.builder()
                .userId(userId)
                .quizId(quiz.getId())
                .status(DailyQuizRedisStatus.UNSOLVED.name())
                .ttl(getTtlUntilNext9AM())
                .build());
    }

    private long getTtlUntilNext9AM() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next9AM = now.withHour(9).withMinute(0).withSecond(0).withNano(0);
        if (!now.isBefore(next9AM)) {
            next9AM = next9AM.plusDays(1);
        }

        return Duration.between(now, next9AM).toSeconds();
    }

    public Optional<UserDailyQuiz> findByIdOptional(Long userId, Long quizId) {
        String id = getId(userId, quizId);

        return dailyQuizRepository.findById(id);
    }

    private String getId(Long userId, Long quizId) {
        return userId + ":" + quizId;
    }

    public void updateStatus(UserDailyQuiz cachedQuiz, DailyQuizRedisStatus status) {
        cachedQuiz.setStatus(status.name());
        dailyQuizRepository.save(cachedQuiz);
    }
}
