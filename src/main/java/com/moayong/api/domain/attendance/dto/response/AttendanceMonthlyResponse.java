package com.moayong.api.domain.attendance.dto.response;

import com.moayong.api.domain.attendance.domain.Attendance;

import java.time.LocalDate;
import java.util.List;

public record AttendanceMonthlyResponse(
        List<LocalDate> attendanceDates
) {
    public static AttendanceMonthlyResponse fromAttendances(List<Attendance> attendances) {

        List<LocalDate> attendanceDates = attendances.stream()
                .map(Attendance::getDate).toList();
        return new AttendanceMonthlyResponse(attendanceDates);
    }
}
