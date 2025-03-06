package com.moayong.api.domain.memberQuiz.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AnswerRequest(
        @Min(1)
        @Max(3)
        @NotNull(message = "답은 Null 일 수 없습니다.")
        Integer answer
) {
}