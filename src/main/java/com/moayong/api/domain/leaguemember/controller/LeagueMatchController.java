package com.moayong.api.domain.leaguemember.controller;

import com.moayong.api.domain.auth.service.AuthService;
import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import com.moayong.api.domain.leaguemember.dto.response.LeagueMemberResponse;
import com.moayong.api.domain.leaguemember.dto.service.MatchInfoServiceDto;
import com.moayong.api.domain.leaguemember.service.LeagueMatchService;
import com.moayong.api.domain.leaguemember.dto.response.MatchInfoResponse;
import com.moayong.api.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LeagueMatchController {
    private final LeagueMatchService matchService;

    @GetMapping("/users/{userId}/match")
    public ApiResponse<MatchInfoResponse> findMatchInfoByUserId(@PathVariable("userId") Long userId) {
        MatchInfoServiceDto matchInfo = matchService.findMatchInfoByUserId(userId);
        MatchInfoResponse response = matchInfo.toResponse();
        return ApiResponse.success(response, "유저 매치정보 조회 성공");
    }

    @PostMapping("/users/{userId}/match")
    public ApiResponse<MatchInfoResponse> matchUserToLeague(@PathVariable("userId") Long userId) {
        LeagueMember member = matchService.matchUserToLeague(userId);
        MatchInfoServiceDto matchInfo = matchService.findMatchInfoByMemberId(member.getId());
        MatchInfoResponse response = matchInfo.toResponse();
        return ApiResponse.success(response, "유저 매칭 성공");
    }

    @GetMapping("/members/{memberId}/match")
    public ApiResponse<MatchInfoResponse> findMatchInfo(@PathVariable("memberId") Long memberId) {
        MatchInfoServiceDto matchInfo = matchService.findMatchInfoByMemberId(memberId);
        MatchInfoResponse response = matchInfo.toResponse();
        return ApiResponse.success(response, "멤버 매치정보 조회 성공");
    }
}
