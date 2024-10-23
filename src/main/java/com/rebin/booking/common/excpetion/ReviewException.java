package com.rebin.booking.common.excpetion;

public class ReviewException extends BadRequestException{
    public ReviewException(ErrorCode errorCode) {
        super(errorCode);
    }
}
