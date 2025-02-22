package com.moayong.api.domain.league.scheduler;

import com.moayong.api.domain.league.repository.LeagueRepository;
import com.moayong.api.domain.league.service.LeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LeagueScheduler {
    private final LeagueService leagueService;

    @Scheduled(cron = "0 0 0 * * MON")
    public void scheduleWeeklyLeagueCreation() {
        leagueService.createLeaguesForNewSeason();
    }
}
