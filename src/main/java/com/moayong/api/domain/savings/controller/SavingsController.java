package com.moayong.api.domain.savings.controller;

import com.moayong.api.domain.savings.domain.Savings;
import com.moayong.api.domain.savings.dto.request.SavingsRequest;
import com.moayong.api.domain.savings.dto.response.SavingsAmountResponse;
import com.moayong.api.domain.savings.dto.response.SavingsResponse;
import com.moayong.api.domain.savings.service.SavingsService;
import com.moayong.api.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SavingsController {
    private final SavingsService savingsService;

    @PostMapping("/members/{memberId}/savings")
    public ApiResponse<SavingsResponse> saveSavings(@PathVariable("memberId") Long memberId, @RequestBody SavingsRequest request) {
        Savings savings = savingsService.saveSavings(memberId, request.toServiceDto());
        SavingsResponse response = new SavingsResponse(savings);
        return ApiResponse.success(response, "저축 인증 성공");
    }

    @GetMapping("/members/{memberId}/savings")
    public ApiResponse<List<SavingsResponse>> findSavingsByMemberId(@PathVariable("memberId") Long memberId) {
        List<Savings> savingsList = savingsService.findSavingsByMemberId(memberId);
        List<SavingsResponse> response = savingsList.stream().map(SavingsResponse::new).toList();
        return ApiResponse.success(response, "멤버 저축 내역 조회");
    }

    @GetMapping("/members/{memberId}/savings/{savingsId}")
    public ApiResponse<SavingsResponse> findSavingsById(@PathVariable("memberId") Long memberId, @PathVariable("savingsId") Long savingsId) {
        Savings savings = savingsService.findSavingsById(savingsId);
        SavingsResponse response = new SavingsResponse(savings);
        return ApiResponse.success(response, "저축 내역 단건 조회");
    }

    @GetMapping("/users/{userId}/savings")
    public ApiResponse<List<SavingsResponse>> findSavingsByUserId(@PathVariable("userId") Long userId) {
        List<Savings> savingsList = savingsService.findSavingsByUserId(userId);
        List<SavingsResponse> response = savingsList.stream().map(SavingsResponse::new).toList();
        return ApiResponse.success(response, "유저 저축 내역 조회");
    }

    @GetMapping("/users/{userId}/savings/total")
    public ApiResponse<SavingsAmountResponse> findSavingsTotalAmountByUserId(@PathVariable("userId") Long userId) {
        Integer amount = savingsService.findSavingsTotalAmountByUserId(userId);
        SavingsAmountResponse response = new SavingsAmountResponse(amount);
        return ApiResponse.success(response, "유저 저축 총액 조회");
    }

    @GetMapping("/members/{memberId}/savings/total")
    public ApiResponse<SavingsAmountResponse> findSavingsTotalAmountByMemberId(@PathVariable("memberId") Long memberId) {
        Integer amount = savingsService.findSavingsTotalAmountByMemberId(memberId);
        SavingsAmountResponse response = new SavingsAmountResponse(amount);
        return ApiResponse.success(response, "멤버 저축 총액 조회");
    }
}