package com.moayong.api.domain.user.service;

import com.moayong.api.domain.league.domain.League;
import com.moayong.api.domain.league.service.LeagueService;
import com.moayong.api.domain.leaguemember.service.LeagueMemberService;
import com.moayong.api.domain.user.domain.UserCurrentLeagueInfo;
import com.moayong.api.domain.auth.enums.AuthErrorCode;
import com.moayong.api.domain.auth.exception.AuthException;
import com.moayong.api.domain.user.repository.UserCurrentLeagueInfoRepository;
import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import com.moayong.api.domain.season.domain.Season;
import com.moayong.api.domain.season.service.SeasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserCurrentLeagueInfoService {
    private final UserCurrentLeagueInfoRepository userInfoRepository;
    private final SeasonService seasonService;
    private final LeagueMemberService memberService;
    private final LeagueService leagueService;

    public Long findLeagueMemberId(Long id) {
        UserCurrentLeagueInfo userInfo = findUserInfo(id);
        return userInfo.getLeagueMemberId();
    }

    public Long findSeasonId(Long id) {
        UserCurrentLeagueInfo userInfo = findUserInfo(id);
        return userInfo.getSeasonId();
    }

    public Long findLeagueId(Long id) {
        UserCurrentLeagueInfo userInfo = findUserInfo(id);
        return userInfo.getLeagueId();
    }

    public Integer findLevel(Long id) {
        UserCurrentLeagueInfo userInfo = findUserInfo(id);
        return userInfo.getLevel();
    }

    public void checkUserInfo(Long id) {
        Optional<UserCurrentLeagueInfo> userInfo = findUserInfoOptional(id);
        if (userInfo.isEmpty()) {
            LeagueMember member = memberService.findRecentMemberByUserId(id);
            if (leagueService.findOpenLeagues().stream().anyMatch(league -> league.getId().equals(member.getLeagueId()))) {
                saveNewSeasonInfo(member);
            } else {
                throw new AuthException(AuthErrorCode.LEAGUE_NOT_MATCH);
            }
        }
    }

    public Optional<UserCurrentLeagueInfo> findUserInfoOptional(Long id) {
        return userInfoRepository.findById(id);
    }

    public UserCurrentLeagueInfo findUserInfo(Long id) {
        return userInfoRepository.findById(id)
                .orElseThrow(() -> new AuthException(AuthErrorCode.LEAGUE_NOT_MATCH));
    }

    public void saveNewSeasonInfo(LeagueMember leagueMember) {
        League league = leagueService.findLeagueById(leagueMember.getLeagueId());
        Season season = seasonService.findSeasonById(league.getSeasonId());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime seasonEndedAt = season.getEndedAt();
        long ttlSeconds = Duration.between(now, seasonEndedAt).getSeconds();

        UserCurrentLeagueInfo userinfo = UserCurrentLeagueInfo.builder()
                .userId(leagueMember.getUserId())
                .seasonId(season.getId())
                .leagueId(leagueMember.getLeagueId())
                .leagueMemberId(leagueMember.getId())
                .level(league.getLevel())
                .ttl(ttlSeconds)
                .build();

        userInfoRepository.save(userinfo);
    }
}
