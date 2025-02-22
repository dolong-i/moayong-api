package com.moayong.api.domain.league.controller;

import com.moayong.api.domain.league.domain.League;
import com.moayong.api.domain.league.dto.response.LeagueResponse;
import com.moayong.api.domain.league.service.LeagueService;
import com.moayong.api.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class LeagueController {
    private final LeagueService leagueService;

    @GetMapping("/api/v1/leagues/open")
    public ApiResponse<List<LeagueResponse>> findOpenLeagues() {
        List<League> leagues = leagueService.findOpenLeagues();
        List<LeagueResponse> response =  leagues.stream().map(LeagueResponse::new).toList();

        return ApiResponse.success(response, "진행중인 리그 조회 성공");
    }

    @GetMapping("/api/v1/leagues/{id}")
    public ApiResponse<LeagueResponse> findLeagueById(@PathVariable("id") Long id) {
        League league = leagueService.findLeagueById(id);
        LeagueResponse response =  new LeagueResponse(league);

        return ApiResponse.success(response, "리그 단건 조회 성공");
    }

    @GetMapping("/api/v1/leagues")
    public ApiResponse<List<LeagueResponse>> findAllLeagues() {
        List<League> leagues = leagueService.findAllLeagues();
        List<LeagueResponse> response =  leagues.stream().map(LeagueResponse::new).toList();

        return ApiResponse.success(response, "리그 전체 조회 성공");
    }

    @PostMapping("/api/v1/leagues/new-season")
    public ApiResponse<Void> createLeaguesForNewSeason() {
        leagueService.createLeaguesForNewSeason();

        return ApiResponse.success(null, "리그 생성 성공");
    }

}
