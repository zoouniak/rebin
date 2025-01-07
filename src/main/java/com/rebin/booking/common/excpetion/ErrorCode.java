package com.rebin.booking.common.excpetion;

public enum ErrorCode {
    SERVER_ERROR("S001", "err.server"),
    INVALID_AUTHORITY("A001", "err.invalid.authority"),
    INVALID_REQUEST("A002", "잘못된 요청입니다."),
    INVALID_ARGUMENT("A003", "err.invalid.argument"),
    INVALID_ADMIN("AD001", "존재하지 않는 관계자입니다."),

    FAIL_GET_USERINFO("L001", "사용자 정보를 불러오는 데 실패하였습니다."),
    INVALID_AUTHORIZE_CODE("L002", "잘못된 코드입니다."),
    NOT_SUPPORT_PROVIDER("L003", "지원하지 않는 소셜 로그인 제공자입니다."),
    EXPIRED_ACCESS_TOKEN("L004", "어세스 토큰이 만료되었습니다."),
    INVALID_TOKEN("L005", "잘못된 토큰입니다."),
    EXPIRED_REFRESH_TOKEN("L006", "리프레시 토큰이 만료되었습니다."),
    INVALID_REFRESH_TOKEN("L007", "잘못된 리프레시 토큰입니다."),
    FAIL_EXTEND("L008", "로그인 연장에 실패하였습니다."),
    INVALID_PRODUCT("P001", "존재하지 않는 상품입니다."),
    INVALID_TIMESLOT("T001", "존재하지 않는 타임슬롯입니다."),
    MAX_ATTEMPTS_EXCEEDED("R001", "예약 코드를 생성하는데 실패하였습니다."),
    RESERVATION_FULL("R002", "이미 예약이 존재합니다."),
    NOT_SUPPORT_STATUS("R003", "지원하지 않는 예약 상태입니다."),
    INVALID_RESERVATION("R004", "존재하지 않는 예약입니다."),
    CANT_CANCEL("R005", "취소가 불가능합니다."),
    CHANGED_NOT_ALLOWED("R006", "변경이 불가능합니다."),
    CONFIRM_REQUEST_NOT_ALLOWED("R007","예약금이 입금된 상태가 아닙니다."),
    REVIEW_NOT_ALLOWED("R008","리뷰 작성 가능한 상태가 아닙니다."),
    ALREADY_WRITE("V001", "이미 리뷰를 작성하였습니다."),
    INVALID_REVIEW("V002", "존재하지 않는 리뷰입니다."),
    INVALID_IMG_PATH("I001", "잘못된 파일 경로입니다."),
    INVALID_IMAGE("I002", "존재하지 않는 이미지입니다."),
    EXCEED_IMAGE_LIST_SIZE("I003", "이미지 최대 크기를 넘었습니다."),
    EMPTY_IMAGE_LIST("I004", "이미지 리스트가 비었습니다."),
    INVALID_NOTICE("N001", "존재하지 않은 공지사항입니다."),
    INVALID_MEMBER("M001", "존재하지 않는 사용자입니다."),
    INVALID_PHONE_FORMAT("M002", "잘못된 번호 형식입니다."),
    EXCEED_NAME_LENGTH("M003", "이름 최대 길이를 초과하였습니다."),
    ALREADY_EXIST("T001","이미 존재하는 타임슬롯입니다."),
    CANT_DELETE_TIMESLOT("T002","해당 시간대에 예약이 존재합니다."),
    INVALID_COMMENT("C001","존재하지 않는 댓글입니다."),
    NOT_COMMENT_AUTHOR("C002","댓글 작성자가 아닙니다.");

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
