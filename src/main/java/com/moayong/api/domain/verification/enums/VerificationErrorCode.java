package com.moayong.api.domain.verification.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum VerificationErrorCode {
    VERIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "검증중인 데이터를 찾을 수 없습니다"),
    WRONG_IMAGE_FILE(HttpStatus.BAD_REQUEST, "잘못된 이미지입니다"),
    VERIFICATION_NOT_FINISHED(HttpStatus.CONFLICT, "검증이 완료되지 않았습니다"),
    ACCOUNT_TEXT_EXTRACT_FAILURE(HttpStatus.BAD_REQUEST, "계좌 텍스트 추출 실패"),
    PAYMENT_TEXT_EXTRACT_FAILURE(HttpStatus.BAD_REQUEST, "거래정보 텍스트 추출 실패"),
    UNSUPPORTED_BANK(HttpStatus.BAD_REQUEST, "아직 지원하지 않는 은행입니다");

    private final HttpStatus status;
    private final String message;
}
