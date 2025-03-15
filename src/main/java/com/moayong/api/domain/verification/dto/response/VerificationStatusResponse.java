package com.moayong.api.domain.verification.dto.response;

import com.moayong.api.domain.verification.domain.Verification;
import com.moayong.api.domain.verification.enums.VerificationStatus;
import lombok.Builder;

@Builder
public record VerificationStatusResponse (
    String id,
    VerificationStatus status
) {
    public VerificationStatusResponse(Verification verification) {
        this(verification.getId(), verification.getStatus());
    }
}
