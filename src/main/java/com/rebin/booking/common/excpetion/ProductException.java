package com.rebin.booking.common.excpetion;

public class ProductException extends RuntimeException {
    private final ErrorCode errorCode;

    public ProductException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }
}
