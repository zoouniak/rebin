package com.rebin.booking.common.excpetion;

public enum ErrorCode {
    SERVER_ERROR(500,"S001","err.server"),
    INVALID_AUTHORITY(403, "A001", "err.invalid.authority"),
    INVALID_REQUEST(400,"A002","err.invalid.request"),
    INVALID_ARGUMENT(400,"A003","err.invalid.argument"),

    FAIL_GET_USERINFO(500, "L001", "err.fail.get-userinfo"),
    INVALID_AUTHORIZE_CODE(500, "L002", "err.invalid.auth-code"),
    NOT_SUPPORT_PROVIDER(400,"L003","err.invalid.provider"),
    EXPIRED_ACCESS_TOKEN(400,"L004","err.expired.access-token"),
    INVALID_TOKEN(400,"L005","err.invalid.token"),
    EXPIRED_REFRESH_TOKEN(400,"L006","err.expired.refresh-token"),
    INVALID_REFRESH_TOKEN(400,"L007","err.invalid.refresh-token"),
    FAIL_EXTEND(403,"L008","err.fail.login-extend"),
    INVALID_PRODUCT(400,"P001","err.invalid.product");
    private final int status;
    private final String code;
    private final String msg;


    ErrorCode(int status, String code, String msg) {
        this.status = status;
        this.code = code;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
