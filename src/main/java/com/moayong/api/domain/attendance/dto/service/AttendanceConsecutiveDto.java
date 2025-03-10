package com.moayong.api.domain.attendance.dto.service;

import com.moayong.api.domain.attendance.dto.response.AttendanceConsecutiveResponse;

public record AttendanceConsecutiveDto(
        int currentConsecutiveDate, // 현재 연속 출석일
        int maxConsecutiveDate // 최대 연속 출석일
) {
    public AttendanceConsecutiveResponse toResponse() {
        return new AttendanceConsecutiveResponse(currentConsecutiveDate, maxConsecutiveDate);
    }
}
