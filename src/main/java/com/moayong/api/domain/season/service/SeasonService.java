package com.moayong.api.domain.season.service;

import com.moayong.api.domain.season.domain.Season;
import com.moayong.api.domain.season.enums.SeasonErrorCode;
import com.moayong.api.domain.season.enums.SeasonStatus;
import com.moayong.api.domain.season.exception.SeasonException;
import com.moayong.api.domain.season.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SeasonService {
    private final SeasonRepository seasonRepository;

    public Season save(Season season) {
        return seasonRepository.save(season);
    }

    public Season findCurrentSeason() {
        return seasonRepository.findCurrentSeason()
                .orElseThrow(() -> new SeasonException(SeasonErrorCode.LEAGUE_NOT_FOUND));
    }

    public Season findSeasonByNumber(Integer number) {
        return seasonRepository.findByNumber(number);
    }

    public Season updateSeasonStatus(Long id, SeasonStatus status) {
        Season season = seasonRepository.findById(id).orElseThrow(() -> new SeasonException(SeasonErrorCode.LEAGUE_NOT_FOUND));;
        season.setStatus(status);
        return seasonRepository.save(season);
    }
}
