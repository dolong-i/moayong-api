package com.moayong.api.domain.user.exception;
import com.moayong.api.domain.user.enums.UserErrorCode;
import com.moayong.api.global.exception.DomainSpecificException;
import lombok.Getter;

import java.util.Map;

@Getter
public class UserException extends DomainSpecificException {
    public UserException(UserErrorCode errorCode, String message, Map<String, Object> errorData) {
        super("User", errorCode.getStatus(), errorCode, message, errorData);
    }

    public UserException(UserErrorCode errorCode) {
        this(errorCode, errorCode.getMessage(), null);
    }

    public UserException(UserErrorCode errorCode, Map<String, Object> errorData) {
        this(errorCode, errorCode.getMessage(), errorData);
    }

    public UserException(UserErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }
}