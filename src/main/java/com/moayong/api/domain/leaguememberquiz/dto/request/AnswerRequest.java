package com.moayong.api.domain.leaguememberquiz.dto.request;

import jakarta.validation.constraints.NotNull;

public record AnswerRequest(
        @NotNull(message = "답은 Null 일 수 없습니다")
        Integer answer
) {
}