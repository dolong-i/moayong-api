package com.moayong.api.domain.season.service;

import com.moayong.api.domain.season.domain.Season;
import com.moayong.api.domain.season.enums.SeasonErrorCode;
import com.moayong.api.domain.season.enums.SeasonStatus;
import com.moayong.api.domain.season.exception.SeasonException;
import com.moayong.api.domain.season.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    public Optional<Season> findOpenSeason() {
        return seasonRepository.findOpenSeason();
    }

    public List<Season> findAllSeasons() {
        return seasonRepository.findAll();
    }

    public Season findSeasonById(Long id) {
        return seasonRepository.findById(id)
                .orElseThrow(() -> {
                    Map<String, Object> errorData = new HashMap<>();
                    errorData.put("id", id);
                    return new SeasonException(SeasonErrorCode.SEASON_NOT_FOUND, errorData);
                });
    }

    public void updateSeasonStatus(Long id, SeasonStatus status) {
        Season season = findSeasonById(id);
        season.setStatus(status);
        seasonRepository.save(season);
    }
}
