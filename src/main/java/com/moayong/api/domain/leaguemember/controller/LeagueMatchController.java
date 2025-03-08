package com.moayong.api.domain.leaguemember.controller;

import com.moayong.api.domain.auth.config.UserPrincipal;
import com.moayong.api.domain.auth.service.AuthService;
import com.moayong.api.domain.leaguemember.dto.response.LeagueMemberResponse;
import com.moayong.api.domain.leaguemember.dto.service.LeagueMemberServiceDto;
import com.moayong.api.domain.leaguemember.dto.service.MatchInfoServiceDto;
import com.moayong.api.domain.leaguemember.service.LeagueMatchService;
import com.moayong.api.domain.leaguemember.dto.response.MatchInfoResponse;
import com.moayong.api.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LeagueMatchController {
    private final AuthService authService;
    private final LeagueMatchService matchService;

    @GetMapping("/users/{id}/match")
    public ApiResponse<MatchInfoResponse> findMatchInfo(@PathVariable("id") Long id,
                                                        @AuthenticationPrincipal UserPrincipal principal) {
        authService.validateUserAccess(id, Long.valueOf(principal.getUserId()));

        MatchInfoServiceDto matchInfo = matchService.findMatchInfo(id);
        MatchInfoResponse response = matchInfo.toResponse();

        return ApiResponse.success(response, "유저 매치정보 조회 성공");
    }

    @PostMapping("/users/{id}/match")
    public ApiResponse<LeagueMemberResponse> matchUserToLeague(@PathVariable("id") Long id,
                                                               @AuthenticationPrincipal UserPrincipal principal) {
        authService.validateUserAccess(id, Long.valueOf(principal.getUserId()));

        LeagueMemberServiceDto memberInfo = matchService.matchUserToLeague(id);
        LeagueMemberResponse response = memberInfo.toResponse();

        return ApiResponse.success(response, "유저 매칭 성공");
    }
}
