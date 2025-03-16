package com.moayong.api.domain.savings.repository;

import com.moayong.api.domain.savings.domain.Savings;

import java.util.List;

public interface SavingsRepositoryCustom {
    List<Savings> findByUserId(Long userId);
    Integer findSavingsTotalAmountByUserId(Long userId);
    Integer findSavingsTotalScoreByLeagueMemberId(Long leagueMemberId);
    Integer findSavingsTotalAmountByLeagueMemberId(Long leagueMemberId);
}
