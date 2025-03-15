package com.moayong.api.domain.verification.service;

import com.moayong.api.domain.user.enums.SavingsBank;
import com.moayong.api.domain.verification.domain.Verification;
import com.moayong.api.domain.verification.dto.service.AccountServiceDto;
import com.moayong.api.domain.verification.dto.service.AccountVerificationServiceDto;
import com.moayong.api.domain.verification.dto.service.PaymentServiceDto;
import com.moayong.api.domain.verification.dto.service.PaymentVerificationServiceDto;
import com.moayong.api.domain.verification.enums.VerificationErrorCode;
import com.moayong.api.domain.verification.enums.VerificationStatus;
import com.moayong.api.domain.verification.exception.VerificationException;
import com.moayong.api.domain.verification.repository.VerificationRepository;
import com.moayong.api.domain.verification.util.OCRTextExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class VerificationService {
    private final VerificationRepository verificationRepository;
    private final AsyncVerificationService asyncVerificationService;
    private final OCRTextExtractor ocrTextExtractor;

    public Verification startVerification(SavingsBank bank, MultipartFile file) {
        byte[] fileBytes = getBytesFromMultipartFile(file);
        String originalFilename = file.getOriginalFilename();
        Verification verification = new Verification(bank);
        verificationRepository.save(verification);
        asyncVerificationService.verifyImage(verification, fileBytes, originalFilename);
        return verification;
    }

    public Verification findByVerificationId(String id) {
        return verificationRepository.findById(id).orElseThrow(() -> {
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("id", id);
            return new VerificationException(VerificationErrorCode.VERIFICATION_NOT_FOUND, errorData);
        });
    }

    public AccountVerificationServiceDto findAccountVerification(String id) {
        Verification verification = findByVerificationId(id);
        validateVerificationFinished(id, verification.getStatus());
        AccountServiceDto account = ocrTextExtractor.extractAccount(verification.getBank(), verification.getOcrText());
        return new AccountVerificationServiceDto(verification, account);
    }

    public PaymentVerificationServiceDto findPaymentVerification(String id) {
        Verification verification = findByVerificationId(id);
        validateVerificationFinished(id, verification.getStatus());
        PaymentServiceDto payment = ocrTextExtractor.extractPayment(verification.getBank(), verification.getOcrText());
        return new PaymentVerificationServiceDto(verification, payment);
    }

    public void validateVerificationFinished(String id, VerificationStatus status) {
        if (status != VerificationStatus.FINISHED) {
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("id", id);
            errorData.put("status", status);
            throw new VerificationException(VerificationErrorCode.VERIFICATION_NOT_FINISHED, errorData);
        }
    }

    private byte[] getBytesFromMultipartFile(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new VerificationException(VerificationErrorCode.WRONG_IMAGE_FILE);
        }
    }
}
