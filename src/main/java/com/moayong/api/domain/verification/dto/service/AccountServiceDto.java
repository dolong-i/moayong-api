package com.moayong.api.domain.verification.dto.service;

import lombok.Builder;

@Builder
public record AccountServiceDto (
    String accountNumber,
    Integer accountBalance
) {

}
