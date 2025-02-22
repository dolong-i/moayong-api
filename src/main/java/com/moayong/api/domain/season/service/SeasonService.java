package com.moayong.api.domain.season.service;

import com.moayong.api.domain.season.domain.Season;
import com.moayong.api.domain.season.enums.SeasonErrorCode;
import com.moayong.api.domain.season.enums.SeasonStatus;
import com.moayong.api.domain.season.exception.SeasonException;
import com.moayong.api.domain.season.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SeasonService {
    private final SeasonRepository seasonRepository;

    public Season save(Season season) {
        return seasonRepository.save(season);
    }

    public Optional<Season> findOpenSeason() {
        return seasonRepository.findOpenSeason();
    }

    public List<Season> findAllSeasons() {
        return seasonRepository.findAll();
    }

    public Season findSeasonById(Long id) {
        return seasonRepository.findById(id)
                .orElseThrow(() -> new SeasonException(SeasonErrorCode.SEASON_NOT_FOUND));
    }

    public void updateSeasonStatus(Long id, SeasonStatus status) {
        Season season = seasonRepository.findById(id)
                .orElseThrow(() -> new SeasonException(SeasonErrorCode.SEASON_NOT_FOUND));
        season.setStatus(status);
        seasonRepository.save(season);
    }

    public Season findSeasonByNumber(Integer number) {
        return seasonRepository.findByNumber(number);
    }
}
