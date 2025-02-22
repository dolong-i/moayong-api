package com.moayong.api.domain.season.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SeasonErrorCode {
    SEASON_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "시즌을 찾을 수 없습니다"),
    CURRENT_SEASON_NOT_OPEN(HttpStatus.INTERNAL_SERVER_ERROR, "현재 시즌이 열려 있지 않습니다");

    private final HttpStatus status;
    private final String message;
}
