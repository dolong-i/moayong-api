package com.moayong.api.domain.attendance.dto.response;

import com.moayong.api.domain.attendance.domain.Attendance;

public record AttendanceDailyResponse(
        boolean attended
) {
    public AttendanceDailyResponse(Attendance attendance) {
        this(
                attendance != null
        );
    }
}
