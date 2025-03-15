package com.moayong.api.domain.verification.controller;

import com.moayong.api.domain.verification.domain.Verification;
import com.moayong.api.domain.verification.dto.request.VerificationRequest;
import com.moayong.api.domain.verification.dto.response.AccountVerificationResponse;
import com.moayong.api.domain.verification.dto.response.PaymentVerificationResponse;
import com.moayong.api.domain.verification.dto.response.VerificationStatusResponse;
import com.moayong.api.domain.verification.dto.service.AccountVerificationServiceDto;
import com.moayong.api.domain.verification.dto.service.PaymentVerificationServiceDto;
import com.moayong.api.domain.verification.service.VerificationService;
import com.moayong.api.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class VerificationController {
    private final VerificationService verificationService;

    @PostMapping("/verification/account/start")
    public ApiResponse<VerificationStatusResponse> startAccountVerification(
            @RequestPart("request") VerificationRequest request,
            @RequestPart("imgFile") MultipartFile file) {
        Verification verification = verificationService.startVerification(request.bank(), file);
        return ApiResponse.success(new VerificationStatusResponse(verification), "검증 시작");
    }

    @GetMapping("/verification/account/{id}/status")
    public ApiResponse<VerificationStatusResponse> findAccountVerificationStatus(@PathVariable String id) {
        Verification verification = verificationService.findByVerificationId(id);
        return ApiResponse.success(new VerificationStatusResponse(verification), "검증 상태 조회 성공");
    }

    @GetMapping("/verification/account/{id}/result")
    public ApiResponse<AccountVerificationResponse> findAccountVerification(@PathVariable String id) {
        AccountVerificationServiceDto accountVerification = verificationService.findAccountVerification(id);
        return ApiResponse.success(accountVerification.toResponse(), "검증 결과 조회 성공");
    }

    @PostMapping("/verification/payment/start")
    public ApiResponse<VerificationStatusResponse> startPaymentVerification(
            @RequestPart("request") VerificationRequest request,
            @RequestPart("imgFile") MultipartFile file) {
        Verification verification = verificationService.startVerification(request.bank(), file);
        return ApiResponse.success(new VerificationStatusResponse(verification), "검증 시작");
    }

    @GetMapping("/verification/payment/{id}/status")
    public ApiResponse<VerificationStatusResponse> findPaymentVerificationStatus(@PathVariable String id) {
        Verification verification = verificationService.findByVerificationId(id);
        return ApiResponse.success(new VerificationStatusResponse(verification), "검증 상태 조회 성공");
    }

    @GetMapping("/verification/payment/{id}/result")
    public ApiResponse<PaymentVerificationResponse> findPaymentVerification(@PathVariable String id) {
        PaymentVerificationServiceDto paymentVerification = verificationService.findPaymentVerification(id);
        return ApiResponse.success(paymentVerification.toResponse(), "검증 결과 조회 성공");
    }
}
