package com.moayong.api.domain.leaguemember.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LeagueMemberErrorCode {
    LEAGUE_MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "사용자가 속한 리그를 찾을 수 없습니다"),
    ACTIVE_LEAGUE_MEMBER_EXISTS(HttpStatus.BAD_REQUEST, "활성화 중인 리그멤버가 존재합니다."),
    ACTIVE_LEAGUE_MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "활성화된 리그멤버를 찾을 수 없습니다"),
    LEAGUE_MEMBER_NOT_FOUND_IN_LEAGUE(HttpStatus.BAD_REQUEST, "해당 리그에 멤버가 없습니다.");

    private final HttpStatus status;
    private final String message;
}