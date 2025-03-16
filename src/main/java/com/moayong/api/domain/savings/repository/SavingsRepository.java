package com.moayong.api.domain.savings.repository;

import com.moayong.api.domain.savings.domain.Savings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavingsRepository extends JpaRepository<Savings, Long>, SavingsRepositoryCustom {
    List<Savings> findByLeagueMemberId(Long memberId);
}
