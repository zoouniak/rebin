package com.rebin.booking.common.excpetion;

public class ProductException extends BadRequestException {
    public ProductException(ErrorCode errorCode) {
        super(errorCode);
    }
}
