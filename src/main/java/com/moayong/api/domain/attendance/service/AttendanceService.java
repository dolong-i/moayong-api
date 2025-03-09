package com.moayong.api.domain.attendance.service;

import com.moayong.api.domain.attendance.domain.Attendance;
import com.moayong.api.domain.attendance.enums.AttendanceErrorCode;
import com.moayong.api.domain.attendance.enums.AttendanceStatus;
import com.moayong.api.domain.attendance.exception.AttendanceException;
import com.moayong.api.domain.attendance.repository.AttendanceRepository;
import com.moayong.api.domain.leaguemember.service.LeagueMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final LeagueMemberService leagueMemberService;

    private Long leagueMemberId = 1L; // TODO 임시 leagueMemberId

    public void saveAttendance(Long userId) {
        LocalDate today = LocalDate.now();

        Optional<Attendance> attendance = attendanceRepository.findByLeagueMemberIdAndDate(leagueMemberId, today);
        if (attendance.isPresent()) {
            throw new AttendanceException(AttendanceErrorCode.ALREADY_ATTENDED_TODAY);
        }

        attendanceRepository.save(
                Attendance.builder()
                        .leagueMemberId(leagueMemberId)
                        .date(today)
                        .status(AttendanceStatus.SUCCESS)
                        .build());
    }

    public Attendance findAttendanceDaily(Long userId) {
        Attendance attendance = attendanceRepository.findByLeagueMemberIdAndDate(leagueMemberId, LocalDate.now())
                .orElse(null);

        return attendance;
    }

    public List<Attendance> findAttendanceMonthly(Long userId, String month) {

        try {
            YearMonth yearMonth;
            if (month == null || month.isBlank()) {
                yearMonth = YearMonth.now();
            } else {
                yearMonth = YearMonth.parse(month, DateTimeFormatter.ofPattern("yyyy-MM"));
            }

            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();

            return attendanceRepository.findAllByLeagueMemberIdAndDateBetweenOrderByDate(leagueMemberId, startDate, endDate);
        } catch (RuntimeException e) {
            throw new AttendanceException(AttendanceErrorCode.WRONG_DATE_FORMAT);
        }
    }
}
