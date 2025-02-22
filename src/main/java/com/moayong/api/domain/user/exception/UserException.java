package com.moayong.api.domain.user.exception;

import com.moayong.api.domain.user.enums.UserErrorCode;
import com.moayong.api.global.exception.DomainSpecificException;
import lombok.Getter;

@Getter
public class UserException extends DomainSpecificException {
    public UserException(UserErrorCode errorCode) {
        super("User", errorCode.getStatus(), errorCode.getMessage());
    }
}
