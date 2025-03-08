package com.moayong.api.domain.attendance.domain;

import com.moayong.api.domain.attendance.enums.AttendanceStatus;
import com.moayong.api.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Attendance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "league_member_id", nullable = false)
    private Long leagueMemberId;

    @Column(name = "date", nullable = false)
    private LocalDate date; // yyyy-MM-dd

    @Column(name = "score")
    private Integer score;

    @Builder
    public Attendance(Long leagueMemberId, LocalDate date, AttendanceStatus status) {
        this.leagueMemberId = leagueMemberId;
        this.date = date;
        this.score = status.getScore();
    }
}
