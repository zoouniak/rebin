package com.rebin.booking.common.excpetion;

public class MemberException extends BadRequestException {
    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
