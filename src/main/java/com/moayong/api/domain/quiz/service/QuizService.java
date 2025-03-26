package com.moayong.api.domain.quiz.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moayong.api.domain.quiz.domain.Quiz;
import com.moayong.api.domain.quiz.dto.service.QuizSaveDto;
import com.moayong.api.domain.quiz.enums.QuizErrorCode;
import com.moayong.api.domain.quiz.exception.QuizException;
import com.moayong.api.domain.quiz.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final ObjectMapper mapper;
    public void saveQuizzes() {
        try {
            Resource resource = new ClassPathResource("template/quiz.json"); // 퀴즈 파일 저장 위치

            QuizSaveDto[] quizSaveDTOS = mapper.readValue(resource.getInputStream(), QuizSaveDto[].class);
            List<QuizSaveDto> quizList = Arrays.asList(quizSaveDTOS);

            // financeTitle 만 추출
            List<String> newQuizTitles = quizList.stream().map(QuizSaveDto::financeTitle).distinct().toList();

            // DB에 있는 financeTitle 조회
            List<String> existingQuizzes = quizRepository.findAllByFinanceTitleIn(newQuizTitles).stream().map(Quiz::getFinanceTitle).toList();

            // 중복되지 않는 퀴즈만 저장
            quizList.stream()
                    .filter(dto -> !existingQuizzes.contains(dto.financeTitle()))
                    .map(QuizSaveDto::toEntity)
                    .forEach(this::save);

        } catch (IOException e) {
            throw new QuizException(QuizErrorCode.QUIZ_SAVE_FAILED);
        }
    }

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
                    errorData.put("quizId", id);
                    return new QuizException(QuizErrorCode.QUIZ_NOT_FOUND, errorData);
                });
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