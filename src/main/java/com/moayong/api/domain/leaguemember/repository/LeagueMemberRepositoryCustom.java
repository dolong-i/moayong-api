package com.moayong.api.domain.leaguemember.repository;

import com.moayong.api.domain.leaguemember.domain.LeagueMember;

import java.util.List;

public interface LeagueMemberRepositoryCustom {
    List<LeagueMember> findByLeagueIdOrderByTotalScoreDesc(Long leagueId);
    LeagueMember findMostRecentLeagueMemberByUserId(Long userId);
}
