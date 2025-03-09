package com.moayong.api.domain.attendance.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AttendanceErrorCode {
    ALREADY_ATTENDED_TODAY(HttpStatus.CONFLICT, "이미 출석체크를 완료했습니다."),
    WRONG_DATE_FORMAT(HttpStatus.BAD_REQUEST, "날짜 형식이 잘못되었습니다.");

    private final HttpStatus status;
    private final String message;
}
