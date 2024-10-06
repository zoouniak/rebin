package com.rebin.booking.common.excpetion;

public class JwtExpiredException extends BadRequestException{
    public JwtExpiredException(ErrorCode errorCode) {
        super(errorCode);
    }
}
