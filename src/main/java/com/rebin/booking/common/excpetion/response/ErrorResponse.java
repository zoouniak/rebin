package com.rebin.booking.common.excpetion.response;

import com.rebin.booking.common.excpetion.ErrorCode;

public record ErrorResponse(
        String code,
        String message
) {
    public static ErrorResponse of(ErrorCode code){
        return new ErrorResponse(code.getCode(),code.getMsg());
    }
}
