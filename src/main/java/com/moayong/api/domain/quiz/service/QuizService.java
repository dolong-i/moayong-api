package com.moayong.api.domain.quiz.service;

import com.moayong.api.domain.quiz.domain.Quiz;
import com.moayong.api.domain.quiz.enums.QuizErrorCode;
import com.moayong.api.domain.quiz.exception.QuizException;
import com.moayong.api.domain.quiz.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;

    public Quiz save(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public void deleteAll() {
        quizRepository.deleteAll();
    }

    public Quiz findQuizById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> {
                    Map<String, Object> errorData = new HashMap<>();
                    errorData.put("id", id);
                    return new QuizException(QuizErrorCode.QUIZ_NOT_FOUND, errorData);
                });
    }

    public Optional<Quiz> findQuizByIdOptional(Long id) {
        return quizRepository.findById(id);
    }

    public List<Quiz> findAllQuizzesById(List<Long> ids) {
        return quizRepository.findAllById(ids);
    }

    public List<Quiz> findAllQuizzesByIdNotIn(List<Long> ids) {
        if (ids.isEmpty()) {
            return quizRepository.findAll();
        }

        return quizRepository.findAllByIdNotIn(ids);
    }

    public List<Quiz> findAllQuizzes() {
        return quizRepository.findAll();
    }

    public long countAllQuizzes() {
        return quizRepository.count();
    }
}
