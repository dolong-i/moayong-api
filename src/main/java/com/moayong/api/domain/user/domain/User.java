package com.moayong.api.domain.user.domain;

import com.moayong.api.domain.auth.enums.AuthProvider;
import com.moayong.api.domain.auth.enums.Role;
import com.moayong.api.domain.user.enums.SavingsBank;
import com.moayong.api.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(name = "UK_provider_providerId", columnNames = {"provider", "provider_id"})
})
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private AuthProvider provider;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "monthly_salary")
    private Integer monthlySalary;

    @Column(name = "savings_rate")
    private Integer savingsRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "savings_bank")
    private SavingsBank savingsBank;

    @Column(name = "account_number")
    private String accountNumber;

    private boolean onboardingCompleted = false; // 온보딩 완료 여부


    @Builder
    public User(AuthProvider provider, String providerId, Role role, String name, String nickname,
                Integer monthlySalary, Integer savingsRate, SavingsBank savingsBank, String accountNumber) {
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
        this.name = name;
        this.nickname = nickname;
        this.monthlySalary = monthlySalary;
        this.savingsRate = savingsRate;
        this.savingsBank = savingsBank;
        this.accountNumber = accountNumber;
    }

    public void completeOnboarding(String name, String nickname, Integer monthlySalary, Integer savingsRate,
                                   SavingsBank savingsBank, String accountNumber) {
        this.name = name;
        this.nickname = nickname;
        this.monthlySalary = monthlySalary;
        this.savingsRate = savingsRate;
        this.savingsBank = savingsBank;
        this.accountNumber = accountNumber;
        this.onboardingCompleted = true;
    }

    public void upgradeToAdmin() {
        this.role = Role.ADMIN;
    }
}