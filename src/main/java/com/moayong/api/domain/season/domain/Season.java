package com.moayong.api.domain.season.domain;

import com.moayong.api.domain.enums.SeasonStatus;
import com.moayong.api.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Season extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "season", nullable = false)
    private Integer season;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private SeasonStatus status;

    @Builder
    public Season(Integer season, SeasonStatus status) {
        this.season = season;
        this.status = status;
    }
}
