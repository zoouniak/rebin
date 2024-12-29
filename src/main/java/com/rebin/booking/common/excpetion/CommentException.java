package com.rebin.booking.common.excpetion;

public class CommentException extends BadRequestException{
    public CommentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
