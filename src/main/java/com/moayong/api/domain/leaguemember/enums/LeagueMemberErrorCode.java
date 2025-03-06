package com.moayong.api.domain.leaguemember.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LeagueMemberErrorCode {
    LEAGUE_MEMBER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "사용자가 속한 리그를 찾을 수 없습니다");

    private final HttpStatus status;
    private final String message;
}