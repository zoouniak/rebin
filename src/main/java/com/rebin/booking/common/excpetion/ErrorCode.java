package com.rebin.booking.common.excpetion;

public enum ErrorCode {
    SERVER_ERROR("S001", "err.server"),
    INVALID_AUTHORITY("A001", "err.invalid.authority"),
    INVALID_REQUEST("A002", "err.invalid.request"),
    INVALID_ARGUMENT("A003", "err.invalid.argument"),

    FAIL_GET_USERINFO("L001", "err.fail.get-userinfo"),
    INVALID_AUTHORIZE_CODE("L002", "err.invalid.auth-code"),
    NOT_SUPPORT_PROVIDER("L003", "err.invalid.provider"),
    EXPIRED_ACCESS_TOKEN("L004", "err.expired.access-token"),
    INVALID_TOKEN("L005", "err.invalid.token"),
    EXPIRED_REFRESH_TOKEN("L006", "err.expired.refresh-token"),
    INVALID_REFRESH_TOKEN("L007", "err.invalid.refresh-token"),
    FAIL_EXTEND("L008", "err.fail.login-extend"),
    INVALID_PRODUCT("P001", "err.invalid.product"),
    INVALID_TIMESLOT("T001", "err.invalid.timeslot"),
    MAX_ATTEMPTS_EXCEEDED("R001", "err.fail.reservation-code"),
    RESERVATION_FULL("R002", "err.already-full.timeslot"),
    NOT_SUPPORT_STATUS("R003", "err.invalid.status"),
    INVALID_RESERVATION("R004", "err.invalid.reservation"),
    CANT_CANCEL("R005", "err.fail-cancel.reservation"),
    ALREADY_WRITE("V001", "err.already-write.review"),
    INVALID_REVIEW("V002", "err.invalid.review"),
    INVALID_IMG_PATH("I001", "err.invalid.path"),
    INVALID_IMAGE("I002","err.invalid.image"),
    EXCEED_IMAGE_LIST_SIZE("I003","err.exceed.image-size"),
    EMPTY_IMAGE_LIST("I004","err.empty.image-size");

    private final String code;
    private final String msg;


    ErrorCode(String code, String msg) {

        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
