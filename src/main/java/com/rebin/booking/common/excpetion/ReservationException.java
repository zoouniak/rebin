package com.rebin.booking.common.excpetion;

public class ReservationException extends BadRequestException{
    public ReservationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
