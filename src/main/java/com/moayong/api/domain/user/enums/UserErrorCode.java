package com.moayong.api.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode {
    USER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "유저를 찾을 수 없습니다"),;

    private final HttpStatus status;
    private final String message;
}
