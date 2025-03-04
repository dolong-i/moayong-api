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

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;

    public Quiz save(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public Quiz findById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> {
                    Map<String, Object> errorData = new HashMap<>();
                    errorData.put("id", id);
                    return new QuizException(QuizErrorCode.QUIZ_NOT_FOUND, errorData);
                });
    }

    public List<Quiz> findAllQuizzes() {
        return quizRepository.findAll();
    }
}
