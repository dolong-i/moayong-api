package com.moayong.api.domain.leaguemember.service;

import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import com.moayong.api.domain.leaguemember.enums.LeagueMemberErrorCode;
import com.moayong.api.domain.leaguemember.enums.LeagueMemberStatus;
import com.moayong.api.domain.leaguemember.exception.LeagueMemberException;
import com.moayong.api.domain.leaguemember.repository.LeagueMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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

    public List<LeagueMember> findAllByUserId(Long userId) {
        return memberRepository.findAllByUserId(userId);
    }

    public List<LeagueMember> findAll() {
        return memberRepository.findAll();
    }

    public LeagueMember findActiveMemberByUserId(Long userId) {
        return memberRepository.findActiveMemberByUserId(userId)
                .orElseThrow(() -> new LeagueMemberException(LeagueMemberErrorCode.ACTIVE_LEAGUE_MEMBER_NOT_FOUND));
    }

    @Transactional
    public void addScore(Long memberId, Integer score) {
        LeagueMember member = findById(memberId);
        member.addScore(score);
    }

    public Integer getRank(List<LeagueMember> members, Long memberId) {// 내 점수가 전체 중에서 상위 몇 %인지 계산
        int totalMembers = members.size();
        return IntStream.range(0, totalMembers)
                .filter(i -> members.get(i).getId().equals(memberId))
                .findFirst()
                .orElse(totalMembers) + 1;
    }

    @Transactional
    public void deactivateAllActiveMembers() {
        memberRepository.updateStatusByCurrentStatus(LeagueMemberStatus.ACTIVE, LeagueMemberStatus.INACTIVE);
    }

    public Float getRate(Integer total, Integer rank) {
        if (total < 3) return 50.0f;

        float rate = ((float) rank / (float) total) * 100;
        return Math.round(rate * 10) / 10.0f;
    }
}
