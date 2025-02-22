package com.moayong.api.domain.season.domain;

import com.moayong.api.domain.season.enums.SeasonPeriod;
import com.moayong.api.domain.season.enums.SeasonStatus;
import com.moayong.api.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Season extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "number", unique = true, nullable = false)
    private Integer number;

    @Setter
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private SeasonStatus status;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "ended_at", nullable = false)
    private LocalDateTime endedAt;

    @Builder
    public Season(Integer number, SeasonStatus status) {
        this.number = number;
        this.status = status;
        this.startedAt = LocalDateTime.now();
        this.endedAt = LocalDateTime.now().plusDays(SeasonPeriod.Week.getPeriod());
    }
}
