package com.moayong.api.domain.season.domain;

import com.moayong.api.domain.season.enums.SeasonStatus;
import com.moayong.api.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
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

    @Column(name = "number", unique = true, nullable = false)
    private Integer number;

    @Setter
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private SeasonStatus status;

    @Builder
    public Season(Integer season, SeasonStatus status) {
        this.number = season;
        this.status = status;
    }
}
