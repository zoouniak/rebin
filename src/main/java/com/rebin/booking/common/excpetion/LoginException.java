package com.rebin.booking.common.excpetion;

public class LoginException extends RuntimeException {
    private final ErrorCode errorCode;

    public LoginException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }
}
