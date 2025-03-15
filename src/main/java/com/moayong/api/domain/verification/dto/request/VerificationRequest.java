package com.moayong.api.domain.verification.dto.request;

import com.moayong.api.domain.user.enums.SavingsBank;

public record VerificationRequest(
    SavingsBank bank
) {
}
