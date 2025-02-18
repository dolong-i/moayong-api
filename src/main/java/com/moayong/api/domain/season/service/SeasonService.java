package com.moayong.api.domain.season.service;

import com.moayong.api.domain.season.domain.Season;
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
}
