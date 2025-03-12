package com.moayong.api.domain.leaguemember.controller;

import com.moayong.api.domain.leaguemember.dto.response.MemberRankingResponse;
import com.moayong.api.domain.leaguemember.dto.service.MemberRankingServiceDto;
import com.moayong.api.domain.leaguemember.service.MemberRankingService;
import com.moayong.api.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberRankingController {
    private final MemberRankingService memberRankingService;

    @GetMapping("/leagues/{leagueId}/members/ranking")
    public ApiResponse<List<MemberRankingResponse>> findMemberRankingsByLeagueId(@PathVariable("leagueId") Long leagueId) {
        List<MemberRankingServiceDto> memberRankings = memberRankingService.findMemberRankingsByLeagueId(leagueId);
        List<MemberRankingResponse> response = memberRankings.stream().map(MemberRankingServiceDto::toResponse).toList();
        return ApiResponse.success(response, "리그내 랭킹 정보 조회 성공");
    }
}
