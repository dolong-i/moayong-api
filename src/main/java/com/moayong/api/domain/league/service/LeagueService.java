package com.moayong.api.domain.league.service;

import com.moayong.api.domain.league.domain.League;
import com.moayong.api.domain.league.enums.LeagueErrorCode;
import com.moayong.api.domain.league.exception.LeagueException;
import com.moayong.api.domain.league.repository.LeagueRepository;
import com.moayong.api.domain.season.domain.Season;
import com.moayong.api.domain.season.enums.SeasonErrorCode;
import com.moayong.api.domain.season.enums.SeasonStatus;
import com.moayong.api.domain.season.exception.SeasonException;
import com.moayong.api.domain.season.service.SeasonService;
import com.moayong.api.global.enums.TierEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LeagueService {
    private final SeasonService seasonService;
    private final LeagueRepository leagueRepository;
    private final StringRedisTemplate redisTemplate;

    public List<League> findOpenLeagues() {
        Season openSeason = seasonService.findOpenSeason()
                .orElseThrow(() -> new SeasonException(SeasonErrorCode.CURRENT_SEASON_NOT_OPEN));

        return leagueRepository.findLeaguesBySeason(openSeason);
    }

    public League findLeagueById(Long id) {
        return leagueRepository.findById(id).orElseThrow(() -> new LeagueException(LeagueErrorCode.LEAGUE_NOT_FOUND));
    }

    public List<League> findAllLeagues() {
        return leagueRepository.findAll();
    }

    @Transactional
    public void createLeaguesForNewSeason() {
        // 락 생성
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("league_creation_lock", "locked", Duration.ofMinutes(5));
        if (lock == null || !lock) {
            System.out.println("다른 인스턴스에서 이미 리그 생성 중입니다.");
            return;
        }

        try {
            System.out.println("리그 생성 시작");

            // 현재 활성화된 시즌 종료
            Season openSeason = seasonService.findOpenSeason().orElse(null);
            if (openSeason != null) {
                seasonService.updateSeasonStatus(openSeason.getId(), SeasonStatus.CLOSE);
            }

            // 새 시즌 생성
            int newSeasonNumber = openSeason != null ? openSeason.getNumber() + 1 : 1;
            Season newSeason = seasonService.save(
                    Season.builder()
                            .status(SeasonStatus.OPEN)
                            .number(newSeasonNumber)
                            .build()
            );

            // 모든 티어에 대해 리그 생성
            List<Integer> tierIds = Arrays.stream(TierEnum.values()).map(TierEnum::getId).toList();
            for (Integer tierId : tierIds) {
                League league = new League(tierId, newSeason);
                leagueRepository.save(league);
            }

            System.out.println("새 시즌과 리그 생성 완료!");
        } finally {
            // 락 해제
            redisTemplate.delete("league_creation_lock");
        }
    }
}
