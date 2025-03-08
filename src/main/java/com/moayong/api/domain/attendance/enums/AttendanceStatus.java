package com.moayong.api.domain.attendance.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AttendanceStatus {
    SUCCESS(5);

    private final Integer score;
}
