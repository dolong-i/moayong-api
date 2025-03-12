package com.moayong.api.domain.leaguemember.service;

import com.moayong.api.domain.league.domain.League;
import com.moayong.api.domain.league.service.LeagueService;
import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import com.moayong.api.domain.leaguemember.dto.service.LeagueMemberWithNickname;
import com.moayong.api.domain.leaguemember.dto.service.MemberRankingServiceDto;
import com.moayong.api.domain.leaguemember.enums.LeagueMemberErrorCode;
import com.moayong.api.domain.leaguemember.enums.PromotionStatus;
import com.moayong.api.domain.leaguemember.exception.LeagueMemberException;
import com.moayong.api.domain.leaguemember.repository.LeagueMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberRankingService {
    private final LeagueMemberService memberService;
    private final LeagueService leagueService;
    private final LeagueMemberRepository memberRepository;

    public List<MemberRankingServiceDto> findMemberRankingsByLeagueId(Long leagueId) {
        // 멤버 목록 가져오기
        List<LeagueMemberWithNickname> membersWithNickname = memberRepository.getLeagueMembersWithNicknameByLeagueId(leagueId);

        // 예외 처리 추가 (멤버가 없는 경우)
        if (membersWithNickname.isEmpty()) {
            throw new LeagueMemberException(LeagueMemberErrorCode.LEAGUE_MEMBER_NOT_FOUND_IN_LEAGUE, Map.of("leagueId", leagueId));
        }

        // 리그 정보 조회
        Long leagueIdFromMember = membersWithNickname.get(0).leagueMember().getLeagueId();
        League league = leagueService.findLeagueById(leagueIdFromMember);

        // 전체 멤버 리스트 추출
        List<LeagueMember> members = membersWithNickname.stream()
                .map(LeagueMemberWithNickname::leagueMember)
                .toList();

        // DTO 변환 및 랭킹 계산
        return membersWithNickname.stream()
                .map(memberWithNickname -> {
                    LeagueMember member = memberWithNickname.leagueMember();
                    Integer rank = memberService.getRank(members, member.getId());
                    Integer rate = memberService.getRate(members.size(), rank);
                    Integer nextLevel = leagueService.getNextLevel(league, rate);
                    PromotionStatus promotionStatus = PromotionStatus.getStatus(league.getLevel(), nextLevel);

                    return MemberRankingServiceDto.builder()
                            .memberId(member.getId())
                            .userId(member.getUserId())
                            .nickname(memberWithNickname.nickname())
                            .promotionStatus(promotionStatus)
                            .rank(rank)
                            .rate(rate)
                            .totalScore(member.getTotalScore())
                            .build();
                })
                .toList();
    }
}
