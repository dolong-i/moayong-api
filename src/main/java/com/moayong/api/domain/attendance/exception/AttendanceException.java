package com.moayong.api.domain.attendance.exception;

import com.moayong.api.domain.attendance.enums.AttendanceErrorCode;
import com.moayong.api.global.exception.DomainSpecificException;
import lombok.Getter;

import java.util.Map;

@Getter
public class AttendanceException extends DomainSpecificException {
    public AttendanceException(AttendanceErrorCode errorCode, String message, Map<String, Object> errorData) {
        super("Attendance", errorCode.getStatus(), errorCode, message, errorData);
    }

    public AttendanceException(AttendanceErrorCode errorCode) {
        this(errorCode, errorCode.getMessage(), null);
    }

    public AttendanceException(AttendanceErrorCode errorCode, Map<String, Object> errorData) {
        this(errorCode, errorCode.getMessage(), errorData);
    }

    public AttendanceException(AttendanceErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }
}
