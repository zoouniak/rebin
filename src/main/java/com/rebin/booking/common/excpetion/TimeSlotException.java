package com.rebin.booking.common.excpetion;

public class TimeSlotException extends BadRequestException {
    public TimeSlotException(ErrorCode errorCode) {
        super(errorCode);
    }
}
