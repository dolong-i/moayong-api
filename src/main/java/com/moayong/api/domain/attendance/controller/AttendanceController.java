package com.moayong.api.domain.attendance.controller;

import com.moayong.api.domain.attendance.domain.Attendance;
import com.moayong.api.domain.attendance.dto.response.AttendanceConsecutiveResponse;
import com.moayong.api.domain.attendance.dto.response.AttendanceDailyResponse;
import com.moayong.api.domain.attendance.dto.response.AttendanceMonthlyResponse;
import com.moayong.api.domain.attendance.dto.service.AttendanceConsecutiveDto;
import com.moayong.api.domain.attendance.service.AttendanceService;
import com.moayong.api.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping("/users/{id}/attendances/today")
    public ApiResponse<Void> saveAttendanceToday(@PathVariable("id") Long userId) {
        attendanceService.saveAttendance(userId);
        return ApiResponse.success(null, "출석체크 성공");
    }

    @GetMapping("/users/{id}/attendances/today")
    public ApiResponse<AttendanceDailyResponse> findAttendanceDaily(@PathVariable("id") Long userId) {
        Attendance attendance = attendanceService.findAttendanceDaily(userId);
        AttendanceDailyResponse response = new AttendanceDailyResponse(attendance);
        return ApiResponse.success(response, "당일 출석 조회 성공");
    }

    @GetMapping("/users/{id}/attendances")
    public ApiResponse<AttendanceMonthlyResponse> findAttendanceMonthly(@PathVariable("id") Long userId,
                                                                        @RequestParam(value = "month", required = false) String month) {
        List<Attendance> attendanceList = attendanceService.findAttendanceMonthly(userId, month);
        AttendanceMonthlyResponse response = AttendanceMonthlyResponse.fromAttendances(attendanceList);
        return ApiResponse.success(response, "월별 출석 조회 성공");
    }

    @GetMapping("/users/{id}/attendances/consecutive")
    public ApiResponse<AttendanceConsecutiveResponse> findConsecutiveAttendances(@PathVariable("id") Long userId) {
        AttendanceConsecutiveDto consecutiveAttendances = attendanceService.findConsecutiveAttendances(userId);
        AttendanceConsecutiveResponse response = consecutiveAttendances.toResponse();
        return ApiResponse.success(response, "연속 출석일 조회 성공");
    }
}
