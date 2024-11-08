package com.rebin.booking.common.excpetion;

public class NoticeException extends BadRequestException {
    public NoticeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
