package com.rebin.booking.common.excpetion;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
    private final String code;
    private final String message;

    public BadRequestException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMsg();
    }
}
