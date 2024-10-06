package com.rebin.booking.common.excpetion;

public class InvalidJwtException extends BadRequestException {
    public InvalidJwtException(ErrorCode errorCode) {
        super(errorCode);

    }
}
