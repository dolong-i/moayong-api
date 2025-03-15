package com.moayong.api.domain.verification.domain;

import com.moayong.api.domain.user.enums.SavingsBank;
import com.moayong.api.domain.verification.enums.VerificationStatus;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Getter
@RedisHash(value = "verification", timeToLive = 5 * 60 * 1000)
public class Verification {
    @Id
    private final String id;
    private final SavingsBank bank;
    private VerificationStatus status;
    private String imageUrl;
    private String ocrText;

    @PersistenceCreator
    public Verification(String id, SavingsBank bank, VerificationStatus status, String imageUrl, String ocrText) {
        this.id = id;
        this.bank = bank;
        this.status = status != null ? status : VerificationStatus.STARTED;
        this.imageUrl = imageUrl;
        this.ocrText = ocrText;
    }

    public Verification(SavingsBank bank) {
        this.id = UUID.randomUUID().toString();
        this.bank = bank;
        this.status = VerificationStatus.STARTED;
    }

    public void startTask() {
        this.status = VerificationStatus.UPLOADING_IMAGE;
    }

    public void uploadImage(String imageUrl) {
        this.imageUrl = imageUrl;
        this.status = VerificationStatus.PROCESSING_OCR;
    }

    public void processOCR(String ocrText) {
        this.ocrText = ocrText;
        this.status = VerificationStatus.FINISHED;
    }

    public void markError() {
        switch(status) {
            case UPLOADING_IMAGE -> this.status = VerificationStatus.IMAGE_UPLOAD_FAILED;
            case PROCESSING_OCR -> this.status = VerificationStatus.OCR_PROCESSING_FAILED;
            case FINISHED -> {}
            default -> this.status = VerificationStatus.SYSTEM_ERROR;
        }
    }
}