package com.rebin.booking.common.excpetion;

public class ImageException extends BadRequestException {
    public ImageException(ErrorCode errorCode) {
        super(errorCode);
    }
}
