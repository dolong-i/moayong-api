package com.moayong.api.domain.memberquiz.service;

import com.moayong.api.domain.memberquiz.domain.UserDailyQuiz;
import com.moayong.api.domain.memberquiz.enums.DailyQuizRedisStatus;
import com.moayong.api.domain.memberquiz.repository.UserDailyQuizRepository;
import com.moayong.api.domain.quiz.domain.Quiz;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDailyQuizService {
    private final UserDailyQuizRepository dailyQuizRepository;

    public List<UserDailyQuiz> findAllByMemberId(Long memberId) {
        return dailyQuizRepository.findAllByMemberId(memberId);
    }

    public void save(Long memberId, Quiz quiz) {
        dailyQuizRepository.save(UserDailyQuiz.builder()
                .memberId(memberId)
                .quizId(quiz.getId())
                .status(DailyQuizRedisStatus.UNSOLVED.name())
                .ttl(getTtlUntilNext9AM())
                .build());
    }

    private long getTtlUntilNext9AM() {
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        LocalDateTime now = zonedDateTime.toLocalDateTime();
        LocalDateTime next9AM = now.withHour(9).withMinute(0).withSecond(0).withNano(0);
        if (!now.isBefore(next9AM)) {
            next9AM = next9AM.plusDays(1);
        }

        return Duration.between(now, next9AM).toSeconds();
    }

    public Optional<UserDailyQuiz> findByIdOptional(Long memberId, Long quizId) {
        String id = getId(memberId, quizId);

        return dailyQuizRepository.findById(id);
    }

    private String getId(Long memberId, Long quizId) {
        return memberId + ":" + quizId;
    }

    public void updateStatus(UserDailyQuiz cachedQuiz, DailyQuizRedisStatus status) {
        cachedQuiz.setStatus(status.name());
        dailyQuizRepository.save(cachedQuiz);
    }
}
