package com.moayong.api.domain.leaguemember.controller;

import com.moayong.api.domain.leaguemember.domain.LeagueMember;
import com.moayong.api.domain.leaguemember.dto.response.LeagueMemberResponse;
import com.moayong.api.domain.leaguemember.service.LeagueMemberService;
import com.moayong.api.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class LeagueMemberController {
    private final LeagueMemberService memberService;

    @GetMapping("/members")
    public ApiResponse<List<LeagueMemberResponse>> findAllMembers() {
        List<LeagueMember> members = memberService.findAll();
        List<LeagueMemberResponse> response = members.stream().map(LeagueMemberResponse::new).toList();
        return ApiResponse.success(response, "멤버 전체 조회 성공");
    }

    @GetMapping("/members/{memberId}")
    public ApiResponse<LeagueMemberResponse> findMemberById(@PathVariable("memberId") Long memberId) {
        LeagueMember member = memberService.findById(memberId);
        LeagueMemberResponse response = new LeagueMemberResponse(member);
        return ApiResponse.success(response, "멤버 단건 조회 성공");
    }

    @GetMapping("/leagues/{leagueId}/members")
    public ApiResponse<List<LeagueMemberResponse>> findMembersByLeagueId(@PathVariable("leagueId") Long leagueId) {
        List<LeagueMember> members = memberService.findByLeagueId(leagueId);
        List<LeagueMemberResponse> response = members.stream().map(LeagueMemberResponse::new).toList();
        return ApiResponse.success(response, "리그내 멤버 전체 조회 성공");
    }

    @GetMapping("/users/{userId}/members")
    public ApiResponse<List<LeagueMemberResponse>> findMembersByUserId(@PathVariable("userId") Long userId) {
        List<LeagueMember> members = memberService.findAllByUserId(userId);
        List<LeagueMemberResponse> response = members.stream().map(LeagueMemberResponse::new).toList();
        return ApiResponse.success(response, "유저 멤버 전체정보 조회 성공");
    }

    @GetMapping("/users/{userId}/members/active")
    public ApiResponse<LeagueMemberResponse> findActiveMemberByUserId(@PathVariable("userId") Long userId) {
        LeagueMember member = memberService.findActiveMemberByUserId(userId);
        LeagueMemberResponse response = new LeagueMemberResponse(member);
        return ApiResponse.success(response, "유저 활성 멤버 조회 성공");
    }
}
