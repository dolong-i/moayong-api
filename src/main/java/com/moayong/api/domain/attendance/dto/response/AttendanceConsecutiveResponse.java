package com.moayong.api.domain.attendance.dto.response;

public record AttendanceConsecutiveResponse(
        int currentConsecutiveDate, // 현재 연속 출석일
        int maxConsecutiveDate // 최대 연속 출석일
) {
}
