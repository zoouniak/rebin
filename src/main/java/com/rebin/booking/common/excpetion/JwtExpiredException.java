package com.rebin.booking.common.excpetion;

public class JwtExpiredException extends RuntimeException {
    private ErrorCode errorCode;

    public JwtExpiredException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }
}
