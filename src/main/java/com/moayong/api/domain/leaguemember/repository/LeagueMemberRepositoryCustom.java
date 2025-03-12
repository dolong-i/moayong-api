package com.moayong.api.domain.leaguemember.repository;

import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import com.moayong.api.domain.leaguemember.dto.service.LeagueMemberWithNickname;

import java.util.List;
import java.util.Optional;

public interface LeagueMemberRepositoryCustom {
    List<LeagueMember> findByLeagueIdOrderByTotalScoreDesc(Long leagueId);
    LeagueMember findMostRecentLeagueMemberByUserId(Long userId);
    Optional<LeagueMember> findActiveMemberByUserId(Long userId);
    List<LeagueMemberWithNickname> getLeagueMembersWithNicknameByLeagueId(Long leagueId);
}
