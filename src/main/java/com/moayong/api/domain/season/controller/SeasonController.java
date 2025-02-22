package com.moayong.api.domain.season.controller;

import com.moayong.api.domain.league.dto.response.LeagueResponse;
import com.moayong.api.domain.season.domain.Season;
import com.moayong.api.domain.season.dto.request.SeasonSaveRequest;
import com.moayong.api.domain.season.dto.response.SeasonResponse;
import com.moayong.api.domain.season.enums.SeasonErrorCode;
import com.moayong.api.domain.season.exception.SeasonException;
import com.moayong.api.domain.season.service.SeasonService;
import com.moayong.api.global.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SeasonController {
    private final SeasonService seasonService;

    @PostMapping("/api/v1/seasons")
    public ApiResponse<SeasonResponse> saveSeason(@RequestBody @Valid SeasonSaveRequest request) {
        Season season = seasonService.save(request.toEntity());
        SeasonResponse response =  new SeasonResponse(season);

        return ApiResponse.success(response, "시즌 등록 성공");
    }

    @GetMapping("/api/v1/seasons")
    public ApiResponse<List<SeasonResponse>> findAllSeasons() {
        List<Season> seasons = seasonService.findAllSeasons();
        List<SeasonResponse> response = seasons.stream().map(SeasonResponse::new).toList();

        return ApiResponse.success(response, "시즌 전체 조회 성공");
    }

    @GetMapping("/api/v1/seasons/{id}")
    public ApiResponse<SeasonResponse> findSeasonById(@PathVariable("id") Long id) {
        Season season = seasonService.findSeasonById(id);
        SeasonResponse response = new SeasonResponse(season);

        return ApiResponse.success(response, "시즌 단건 조회 성공");
    }

    @GetMapping("/api/v1/seasons/open")
    public ApiResponse<SeasonResponse> findOpenSeason() {
        Season season = seasonService.findOpenSeason()
                .orElseThrow(() -> new SeasonException(SeasonErrorCode.CURRENT_SEASON_NOT_OPEN));
        SeasonResponse response = new SeasonResponse(season);

        return ApiResponse.success(response, "현재 시즌 조회 성공");
    }
}
