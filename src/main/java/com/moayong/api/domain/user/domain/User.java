package com.moayong.api.domain.user.domain;

import com.moayong.api.domain.auth.domain.UserTemporary;
import com.moayong.api.domain.auth.dto.service.OnboardingServiceDto;
import com.moayong.api.domain.auth.enums.AuthProvider;
import com.moayong.api.domain.auth.enums.Role;
import com.moayong.api.domain.user.enums.SavingsBank;
import com.moayong.api.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Setter
    @Column(name = "nickname")
    private String nickname;

    @Column(name = "email")
    private String email;

    @Setter
    @Column(name = "monthly_salary")
    private Integer monthlySalary;

    @Setter
    @Column(name = "savings_rate")
    private Integer savingsRate;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "savings_bank")
    private SavingsBank savingsBank;

    @Setter
    @Column(name = "account_number")
    private String accountNumber;

    @Setter
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public User(AuthProvider provider, String providerId, Role role, String email, String name, String nickname,
                Integer monthlySalary, Integer savingsRate, SavingsBank savingsBank, String accountNumber) {
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.monthlySalary = monthlySalary;
        this.savingsRate = savingsRate;
        this.savingsBank = savingsBank;
        this.accountNumber = accountNumber;
    }

    public User(UserTemporary userTemporary, OnboardingServiceDto onboardingServiceDto) {
        this.provider = userTemporary.getProvider();
        this.providerId = userTemporary.getProviderId();
        this.role = userTemporary.getRole();
        this.email = userTemporary.getEmail();
        this.name = onboardingServiceDto.name();
        this.nickname = onboardingServiceDto.nickname();
        this.monthlySalary = onboardingServiceDto.monthlySalary();
        this.savingsRate = onboardingServiceDto.savingsRate();
        this.savingsBank = onboardingServiceDto.savingsBank();
        this.accountNumber = onboardingServiceDto.accountNumber();
    }

    public void upgradeToAdmin() {
        this.role = Role.ADMIN;
    }

    public Integer getGoalAmount() {
        return monthlySalary * savingsRate / 100;
    }
}