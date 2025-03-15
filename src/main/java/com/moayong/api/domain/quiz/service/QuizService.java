package com.moayong.api.domain.quiz.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moayong.api.domain.quiz.domain.Quiz;
import com.moayong.api.domain.quiz.enums.QuizErrorCode;
import com.moayong.api.domain.quiz.exception.QuizException;
import com.moayong.api.domain.quiz.repository.QuizRepository;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @PostConstruct // TODO 테스트용
    public void initQuiz() {
        try {
            Resource resource = new ClassPathResource("template/quiz.json");
            ObjectMapper mapper = new ObjectMapper();
            QuizSaveRequest[] quizSaveRequests = mapper.readValue(resource.getInputStream(), QuizSaveRequest[].class);
            List<QuizSaveRequest> quizList = Arrays.asList(quizSaveRequests);

            quizList.forEach(request -> {
                Quiz quiz = request.toEntity();
                save(quiz);
            });
            quizList.forEach(request -> {
                Quiz quiz = request.toEntity();
                save(quiz);
            });
        } catch (IOException e) {
            throw new RuntimeException("Quiz 임포트 실패", e);
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

// TODO 테스트용
record QuizSaveRequest(
        @JsonProperty("problem_title")
        @NotBlank(message = "퀴즈 제목은 빈 값일 수 없습니다.")
        String problemTitle,

        @JsonProperty("answer_description")
        @NotBlank(message = "퀴즈 설명은 빈 값일 수 없습니다.")
        String answerDescription,

        @JsonProperty("problem_options")
        @NotEmpty(message = "퀴즈 옵션은 최소 하나 이상 있어야 합니다.")
        List<String> problemOptions,

        @JsonProperty("answer_number")
        @NotNull(message = "정답 번호는 null일 수 없습니다.")
        Integer answerNumber,

        @JsonProperty("finance_title")
        @NotBlank(message = "금융정보 제목은 빈 값일 수 없습니다.")
        String financeTitle,

        @JsonProperty("finance_description")
        @NotBlank(message = "금융정보 설명은 빈 값일 수 없습니다.")
        String financeDescription,

        @JsonProperty("source_title")
        @NotBlank(message = "출처 제목은 빈 값일 수 없습니다.")
        String sourceTitle,

        @JsonProperty("source_link")
        @NotBlank(message = "출처 링크는 빈 값일 수 없습니다.")
        String sourceLink

) {
    public Quiz toEntity() {
        return Quiz.builder()
                .financeTitle(financeTitle)
                .financeDescription(financeDescription)
                .problemTitle(problemTitle)
                .problemOptions(problemOptions)
                .answerNumber(answerNumber)
                .answerDescription(answerDescription)
                .sourceTitle(sourceTitle)
                .sourceLink(sourceLink)
                .build();
    }
}