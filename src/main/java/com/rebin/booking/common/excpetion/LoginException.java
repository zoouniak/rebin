package com.rebin.booking.common.excpetion;

public class LoginException extends BadRequestException {
    public LoginException(ErrorCode errorCode) {
        super(errorCode);
    }
}
