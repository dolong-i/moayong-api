package com.moayong.api.domain.savings.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Savings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "league_member_id", nullable = false)
    private Long leagueMemberId;

    @Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private LocalDateTime datetime;

    @Builder
    public Savings(Long leagueMemberId, String imageUrl, Integer score, Integer amount, LocalDateTime datetime) {
        this.leagueMemberId = leagueMemberId;
        this.imageUrl = imageUrl;
        this.score = score;
        this.amount = amount;
        this.datetime = datetime;
    }
}
