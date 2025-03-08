package com.moayong.api.domain.attendance.repository;

import com.moayong.api.domain.attendance.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByLeagueMemberIdAndDate(Long leagueMemberId, LocalDate date);

    List<Attendance> findAllByLeagueMemberIdAndDateBetweenOrderByDate(Long leagueMemberId, LocalDate startDate, LocalDate endDate);

}
