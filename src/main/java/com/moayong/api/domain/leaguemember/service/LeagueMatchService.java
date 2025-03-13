package com.moayong.api.domain.leaguemember.service;

import com.moayong.api.domain.league.domain.League;
import com.moayong.api.domain.league.service.LeagueService;
import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import com.moayong.api.domain.leaguemember.dto.service.MatchInfoServiceDto;
import com.moayong.api.domain.leaguemember.enums.LeagueMemberErrorCode;
import com.moayong.api.domain.leaguemember.enums.LeagueMemberStatus;
import com.moayong.api.domain.leaguemember.enums.PromotionStatus;
import com.moayong.api.domain.leaguemember.exception.LeagueMemberException;
import com.moayong.api.domain.user.domain.User;
import com.moayong.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeagueMatchService {
    private final LeagueService leagueService;
    private final LeagueMemberService memberService;
    private final UserService userService;

    public MatchInfoServiceDto findMatchInfoByUserId(Long userId) {
        LeagueMember prevMember = memberService.findRecentMemberByUserId(userId);
        if (prevMember.getStatus() == LeagueMemberStatus.ACTIVE) {
            throw new LeagueMemberException(LeagueMemberErrorCode.ACTIVE_LEAGUE_MEMBER_EXISTS, Map.of("memberId", prevMember.getId()));
        }
        return findMatchInfoByMemberId(prevMember.getId());
    }

    public LeagueMember matchUserToLeagueByLevel(User user, Integer level) {
        League league = leagueService.findOpenLeagueByLevel(level);
        Integer goalAmount = user.getGoalAmount();
        LeagueMember member = LeagueMember.builder()
                .userId(user.getId())
                .leagueId(league.getId())
                .totalScore(0)
                .goalAmount(goalAmount)
                .status(LeagueMemberStatus.ACTIVE)
                .build();
        return memberService.save(member);
    }

    public LeagueMember matchUserToLeague(Long userId) {
        MatchInfoServiceDto matchInfo = findMatchInfoByUserId(userId);
        User user = userService.findUserById(userId);
        return matchUserToLeagueByLevel(user, matchInfo.nextLevel());
    }

    public MatchInfoServiceDto findMatchInfoByMemberId(Long memberId) {
        LeagueMember prevMember = memberService.findById(memberId);
        League prevLeague = leagueService.findLeagueById(prevMember.getLeagueId());

        // 총점을 기준으로 내림차순 가져오기
        List<LeagueMember> leagueMembers = memberService.findByLeagueIdOrderByScore(prevLeague.getId());

        Integer rank = memberService.getRank(leagueMembers, prevMember.getId());
        Integer rate = memberService.getRate(leagueMembers.size(), rank);
        Integer nextLevel = leagueService.getNextLevel(prevLeague, rate);
        PromotionStatus promotionStatus = PromotionStatus.getStatus(prevLeague.getLevel(), nextLevel);

        return MatchInfoServiceDto.builder()
                .memberId(prevMember.getId())
                .leagueId(prevMember.getLeagueId())
                .totalScore(prevMember.getTotalScore())
                .goalAmount(prevMember.getGoalAmount())
                .rank(rank)
                .rate(rate)
                .promotionStatus(promotionStatus)
                .nextLevel(nextLevel)
                .build();
    }
}
