package com.rebin.booking.common.excpetion;

public class AdminException extends BadRequestException {
    public AdminException(ErrorCode errorCode) {
        super(errorCode);
    }
}
