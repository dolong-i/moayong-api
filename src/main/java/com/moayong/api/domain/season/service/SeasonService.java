package com.moayong.api.domain.season.service;

import com.moayong.api.domain.season.domain.Season;
import com.moayong.api.domain.season.enums.SeasonErrorCode;
import com.moayong.api.domain.season.enums.SeasonStatus;
import com.moayong.api.domain.season.exception.SeasonException;
import com.moayong.api.domain.season.repository.SeasonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SeasonService {
    private final SeasonRepository seasonRepository;

    public Season save(Season season) {
        return seasonRepository.save(season);
    }

    public Season findOpenSeason() {
        return seasonRepository.findOpenSeason().orElseThrow(() -> new SeasonException(SeasonErrorCode.CURRENT_SEASON_NOT_OPEN));
    }

    public Optional<Season> findOpenSeasonOptional() {
        return seasonRepository.findOpenSeason();
    }

    public List<Season> findAllSeasons() {
        return seasonRepository.findAll();
    }

    public Season findSeasonById(Long id) {
        return seasonRepository.findById(id)
                .orElseThrow(() -> new SeasonException(SeasonErrorCode.SEASON_NOT_FOUND, Map.of("id", id)));
    }

    @Transactional
    public void updateSeasonStatus(Long id, SeasonStatus status) {
        Season season = findSeasonById(id);
        season.setStatus(status);
    }
}
