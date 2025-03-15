package com.moayong.api.domain.savings.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SavingsErrorCode {
    SAVINGS_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 저축 기록을 찾을 수 없습니다."),
    WITHDRAWAL_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "출금은 허용되지 않습니다."),
    USER_NAME_MISMATCH(HttpStatus.BAD_REQUEST, "유저의 등록된 이름과 계좌의 이름이 일치하지 않습니다."),
    NOT_WITHIN_CURRENT_SEASON(HttpStatus.BAD_REQUEST, "저축한 날짜가 현재 시즌에 속하지 않습니다."),
    BALANCE_MISMATCH_BEFORE_SAVINGS(HttpStatus.BAD_REQUEST, "거래 이전 잔액이 이전 총 저축 금액과 일치하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}