package com.moayong.api.domain.leaguemember.service;

import com.moayong.api.domain.league.domain.League;
import com.moayong.api.domain.league.service.LeagueService;
import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import com.moayong.api.domain.leaguemember.dto.service.LeagueMemberServiceDto;
import com.moayong.api.domain.leaguemember.dto.service.MatchInfoServiceDto;
import com.moayong.api.domain.user.domain.User;
import com.moayong.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeagueMatchService {
    private final LeagueService leagueService;
    private final LeagueMemberService memberService;
    private final UserService userService;

    public MatchInfoServiceDto findMatchInfo(Long userId) {
        LeagueMemberServiceDto prevLeagueMember = getPrevLeagueMember(userId);
        Integer nextLevel = getNextLevel(prevLeagueMember.league(), prevLeagueMember.rate());
        LeagueMemberServiceDto nextLeagueMember = getNextLeagueMember(userId, nextLevel);

        return MatchInfoServiceDto.builder()
                .userId(userId)
                .prevMemberInfo(prevLeagueMember)
                .nextMemberInfo(nextLeagueMember)
                .build();
    }

    public LeagueMemberServiceDto getPrevLeagueMember(Long userId) {
        LeagueMember prevMember = memberService.findRecentMemberByUserId(userId);
        League prevLeague = leagueService.findLeagueById(prevMember.getLeagueId());

        // 총점을 기준으로 내림차순 가져오기
        List<LeagueMember> leagueMembers = memberService.findByLeagueIdOrderByScore(prevLeague.getId());

        // 내 점수가 전체 중에서 상위 몇 %인지 계산
        int totalMembers = leagueMembers.size();
        int myRank = IntStream.range(0, totalMembers)
                .filter(i -> leagueMembers.get(i).getId().equals(prevMember.getId()))
                .findFirst()
                .orElse(totalMembers) + 1;  // 만약 못 찾으면 최하위로 가정

        int myPercent = (int)Math.round(((double) myRank / totalMembers) * 100);

        // TODO: 총인원이 적을때 정책 정해야함
        if (totalMembers < 3) {
            myPercent = 50;
        }

        return LeagueMemberServiceDto.builder()
                .userId(userId)
                .totalScore(prevMember.getTotalScore())
                .goalAmount(prevMember.getGoalAmount())
                .rank(myRank)
                .rate(myPercent)
                .league(prevLeague)
                .build();
    }

    public LeagueMemberServiceDto getNextLeagueMember(Long userId, Integer level) {
        User user = userService.findUserById(userId);
        Integer goalAmount = user.getGoalAmount();
        League league = leagueService.findOpenLeagueByLevel(level);
        int totalMembers = memberService.findByLeagueId(league.getId()).size();

        return LeagueMemberServiceDto.builder()
                .userId(userId)
                .totalScore(0)
                .goalAmount(goalAmount)
                .rank(totalMembers)
                .rate(100)
                .league(league)
                .build();
    }

    public Integer getNextLevel(League league, int rate) {
        int level = league.getLevel();

        if (rate <= league.getPromotionRate()) return level + 1;
        if (rate > league.getRelegationRate()) return level - 1;

        return level;
    }

    public LeagueMember matchUserToLeagueByLevel(User user, Integer level) {
        League league = leagueService.findOpenLeagueByLevel(level);
        Integer goalAmount = user.getGoalAmount();
        LeagueMember member = LeagueMember.builder()
                .userId(user.getId())
                .leagueId(league.getId())
                .totalScore(0)
                .goalAmount(goalAmount)
                .build();
        return memberService.save(member);
    }

    public LeagueMemberServiceDto matchUserToLeague(Long userId) {
        LeagueMemberServiceDto memberServiceDto = findMatchInfo(userId).nextMemberInfo();
        LeagueMember leagueMember = memberServiceDto.toEntity();
        memberService.save(leagueMember);
        return memberServiceDto;
    }
}
