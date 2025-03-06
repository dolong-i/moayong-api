package com.moayong.api.domain.leaguemember.service;

import com.moayong.api.domain.league.domain.League;
import com.moayong.api.domain.league.service.LeagueService;
import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import com.moayong.api.domain.leaguemember.repository.LeagueMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeagueMemberService {
    private final LeagueMemberRepository leagueMemberRepository;
    private final LeagueService leagueService;

    public Optional<LeagueMember> findCurrentLeagueMemberOptional(Long userId) {
        List<League> openLeagues = leagueService.findOpenLeagues();
        List<Long> leagueIds = openLeagues.stream().map(League::getId).toList();

        return leagueMemberRepository.findLeagueMemberByUserAndLeagues(userId, leagueIds);
    }
}
