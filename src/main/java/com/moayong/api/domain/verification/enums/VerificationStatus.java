package com.moayong.api.domain.verification.enums;

public enum VerificationStatus {
    STARTED,            // 검증 시작
    SYSTEM_ERROR,       // 알수없는 에러
    UPLOADING_IMAGE,    // S3에 이미지 업로드 중
    IMAGE_UPLOAD_FAILED,  // S3 업로드 실패
    PROCESSING_OCR,     // OCR 처리 중
    OCR_PROCESSING_FAILED,// OCR 처리 실패
    FINISHED            // 검증 완료
}