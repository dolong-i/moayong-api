package com.moayong.api.domain.leaguemember.service;

import com.moayong.api.domain.league.service.LeagueService;
import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import com.moayong.api.domain.leaguemember.enums.LeagueMemberErrorCode;
import com.moayong.api.domain.leaguemember.exception.LeagueMemberException;
import com.moayong.api.domain.leaguemember.repository.LeagueMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LeagueMemberService {
    private final LeagueMemberRepository memberRepository;

    public LeagueMember findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> {
                    Map<String, Object> errorData = new HashMap<>();
                    errorData.put("id", id);
                    return new LeagueMemberException(LeagueMemberErrorCode.LEAGUE_MEMBER_NOT_FOUND, errorData);
                });
    }

    public List<LeagueMember> findByLeagueIdOrderByScore (Long LeagueId) {
        return memberRepository.findByLeagueIdOrderByTotalScoreDesc(LeagueId);
    }

    public LeagueMember findRecentMemberByUserId(Long userId) {
        return memberRepository.findMostRecentLeagueMemberByUserId(userId);
    }

    public List<LeagueMember> findByLeagueId(Long leagueId) {
        return memberRepository.findByLeagueId(leagueId);
    }

    public LeagueMember save(LeagueMember leagueMember) {
        return memberRepository.save(leagueMember);
    }
}
