package com.moayong.api.domain.attendance.service;

import com.moayong.api.domain.attendance.domain.Attendance;
import com.moayong.api.domain.attendance.dto.service.AttendanceConsecutiveDto;
import com.moayong.api.domain.attendance.enums.AttendanceErrorCode;
import com.moayong.api.domain.attendance.enums.AttendanceStatus;
import com.moayong.api.domain.attendance.exception.AttendanceException;
import com.moayong.api.domain.attendance.repository.AttendanceRepository;
import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import com.moayong.api.domain.leaguemember.service.LeagueMemberService;
import com.moayong.api.domain.user.service.UserCurrentLeagueInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final UserCurrentLeagueInfoService userInfoService;
    private final LeagueMemberService leagueMemberService;

    public void saveAttendance(Long userId) {
        LocalDate today = LocalDate.now();

        Long leagueMemberId = userInfoService.findLeagueMemberId(userId);

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
        Long leagueMemberId = userInfoService.findLeagueMemberId(userId);

        return attendanceRepository.findByLeagueMemberIdAndDate(leagueMemberId, LocalDate.now())
                .orElse(null);
    }

    public List<Attendance> findAttendanceMonthly(Long userId, String month) {
        List<Long> leagueMemberIds;

        try {
            YearMonth yearMonth;
            if (month == null || month.isBlank()) {
                yearMonth = YearMonth.now();
                leagueMemberIds = Collections.singletonList(userInfoService.findLeagueMemberId(userId));
            } else {
                yearMonth = YearMonth.parse(month, DateTimeFormatter.ofPattern("yyyy-MM"));
                leagueMemberIds = leagueMemberService.findAllByUserId(userId)
                        .stream().map(LeagueMember::getId).toList();
            }

            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();

            return attendanceRepository.findAllByLeagueMemberIdInAndDateBetweenOrderByDate(leagueMemberIds, startDate, endDate);
        } catch (RuntimeException e) {
            throw new AttendanceException(AttendanceErrorCode.WRONG_DATE_FORMAT);
        }
    }

    public AttendanceConsecutiveDto findConsecutiveAttendances(Long userId) {
        List<Long> leagueMemberIds = leagueMemberService.findAllByUserId(userId)
                .stream().map(LeagueMember::getId).toList();

        List<Attendance> allAttendances = attendanceRepository.findAllByLeagueMemberIdInOrderByDate(leagueMemberIds);
        if (allAttendances.isEmpty()) {
            return new AttendanceConsecutiveDto(0, 0);
        }

        int maxConsecutive = 0;
        int tempConsecutive = 0;
        LocalDate previousDate = null;

        for (Attendance attendance : allAttendances) {
            LocalDate date = attendance.getDate();
            if (previousDate == null) {
                tempConsecutive = 1;
            } else {
                // 이전 출석일의 다음날과 현재 날짜가 일치하면 연속
                if (date.equals(previousDate.plusDays(1))) {
                    tempConsecutive++;
                } else {
                    tempConsecutive = 1;
                }
            }

            if (tempConsecutive > maxConsecutive) {
                maxConsecutive = tempConsecutive;
            }

            previousDate = date;
        }

        int currentConsecutive = 0;
        // 현재 연속 출석일: 마지막 출석 기록이 오늘이면 tempConsecutive 값
        if (allAttendances.get(allAttendances.size() - 1).getDate().isEqual(LocalDate.now())) {
            currentConsecutive = tempConsecutive;
        }

        return new AttendanceConsecutiveDto(currentConsecutive, maxConsecutive);
    }
}
