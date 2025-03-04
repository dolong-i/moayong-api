package com.moayong.api.domain.auth.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력 값입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 아이디입니다."),
    FAIL_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "로그인 정보가 정확하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "권한이 없습니다"),
    ONBOARDING_ACCESS_ONLY(HttpStatus.FORBIDDEN, "온보딩이 완료되지 않았습니다. 온보딩을 완료한 후 다시 시도하세요."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류입니다"),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "JWT 형식이 다릅니다"),
    EMPTY_JWT_CLAIMS(HttpStatus.BAD_REQUEST, "JWT 토큰이 없습니다."),
    SUCCESS(HttpStatus.OK, "성공"),
    ;

    private final HttpStatus status;
    private final String message;
}
