package com.moayong.api.domain.quiz.dto.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moayong.api.domain.quiz.domain.Quiz;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record QuizSaveDto(
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
