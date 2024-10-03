package com.rebin.booking.common.excpetion;

public class InvalidJwtException extends RuntimeException {
    private ErrorCode errorCode;

    public InvalidJwtException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }
}
