package com.moayong.api.domain.memberquiz.dto.request;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record AnswerRequest(
        @Range(min = 1L, max = 3L, message = "답은 1에서 3 사이여야 합니다.")
        @NotNull(message = "답은 Null 일 수 없습니다.")
        Integer answer
) {
}