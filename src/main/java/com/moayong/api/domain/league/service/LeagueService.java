package com.moayong.api.domain.league.service;

import com.moayong.api.domain.league.domain.League;
import com.moayong.api.domain.league.enums.LeagueErrorCode;
import com.moayong.api.domain.league.exception.LeagueException;
import com.moayong.api.domain.league.repository.LeagueRepository;
import com.moayong.api.domain.leaguemember.service.LeagueMemberService;
import com.moayong.api.domain.season.domain.Season;
import com.moayong.api.domain.season.enums.SeasonStatus;
import com.moayong.api.domain.season.service.SeasonService;
import com.moayong.api.domain.league.enums.Tier;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class LeagueService {
    private final SeasonService seasonService;
    private final LeagueRepository leagueRepository;
    private final StringRedisTemplate redisTemplate;

    // TODO: 테스트용 코드 운영때는 미리 새로운 시즌을 만들어둬야할듯
    @PostConstruct
    public void init() {
        // 애플리케이션 시작 후 한 번 실행할 로직
        createLeaguesForNewSeason();
    }

    public List<League> findOpenLeagues() {
        Season openSeason = seasonService.findOpenSeason();

        return leagueRepository.findLeaguesBySeasonId(openSeason.getId());
    }

    public League findLeagueById(Long id) {
        return leagueRepository.findById(id).orElseThrow(() -> {
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("id", id);
            return new LeagueException(LeagueErrorCode.LEAGUE_NOT_FOUND, errorData);
        });
    }

    public List<League> findAllLeagues() {
        return leagueRepository.findAll();
    }

    @Transactional
    public void createLeaguesForNewSeason() {
        // 락 생성
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("league_creation_lock", "locked", Duration.ofMinutes(5));
        if (lock == null || !lock) {
            log.info("다른 인스턴스에서 리그 생성 중입니다. 리그 생성 요청을 취소합니다.");
            return;
        }

        try {
            log.info("리그 생성 시작");

            // 현재 활성화된 시즌 종료
            Season openSeason = seasonService.findOpenSeasonOptional().orElse(null);
            if (openSeason != null) {
                seasonService.updateSeasonStatus(openSeason.getId(), SeasonStatus.CLOSE);
                log.info("시즌 {} 종료", openSeason.getNumber());
            }

            // 새 시즌 생성
            int newSeasonNumber = openSeason != null ? openSeason.getNumber() + 1 : 1;
            Season newSeason = seasonService.save(
                    Season.builder()
                            .status(SeasonStatus.OPEN)
                            .number(newSeasonNumber)
                            .build()
            );
            log.info("새 시즌 {} 생성", newSeasonNumber);

            // 모든 티어에 대해 리그 생성
            Tier[] tiers = Tier.values();
            for (Tier tier : tiers) {
                League league = new League(newSeason.getId(), tier);
                leagueRepository.save(league);
                log.info("티어 {}에 대해 리그 생성", tier.name());
            }

            log.info("새 시즌과 리그 생성 완료!");
        } finally {
            // 락 해제
            redisTemplate.delete("league_creation_lock");
        }
    }

    // 특정 티어의 열린 리그 찾기
    public League findOpenLeagueByLevel(Integer level) {
        return findOpenLeagues()
                .stream()
                .filter(league -> league.getLevel().equals(level))
                .findFirst()
                .orElse(null);
    }
}
