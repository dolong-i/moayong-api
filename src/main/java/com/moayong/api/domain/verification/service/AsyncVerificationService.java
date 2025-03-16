package com.moayong.api.domain.verification.service;

import com.moayong.api.domain.verification.domain.Verification;
import com.moayong.api.domain.verification.repository.VerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncVerificationService {
    private final VerificationRepository verificationRepository;
    private final S3Service s3Service;
    private final OCRService ocrService;

    @Async("asyncVerificationExecutor")
    public void verifyImage(Verification verification, byte[] fileBytes, String originalFilename) {
        try {
            verification.startTask();
            verificationRepository.save(verification);

            String imageUrl = s3Service.uploadFile(fileBytes, originalFilename);
            verification.uploadImage(imageUrl);
            verificationRepository.save(verification);

            String extractedText = ocrService.extractTextFromImage(fileBytes);
            verification.processOCR(extractedText);

            verificationRepository.save(verification);
        } catch (Exception e) {
            log.error("Account verification failed: {}", verification.getId(), e);
            verification.markError();
            verificationRepository.save(verification);
        }
    }

}
