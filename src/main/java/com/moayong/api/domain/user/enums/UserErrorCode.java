package com.moayong.api.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode {
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "유저를 찾을 수 없습니다"),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "이미 등록된 닉네임이에요."),
    TOTAL_SAVINGS_NOT_MATCH(HttpStatus.BAD_REQUEST, "통장의 누적 저축금액이 동일하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}
