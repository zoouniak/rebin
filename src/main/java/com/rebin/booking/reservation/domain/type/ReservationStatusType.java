package com.rebin.booking.reservation.domain.type;

public enum ReservationStatusType {
    PENDING_PAYMENT,    // 예약금 입금 요청 전
    CONFIRM_REQUESTED,  // 입금 확인 요청 보냄
    PAYMENT_CONFIRMED,  // 입금 완료
    SHOOTING_COMPLETED, // 촬영 완료
    REVIEW_COMPLETED,   // 리뷰 작성 완료
    CANCELED,            // 예약 취소됨
    CHANGED // 예약 변경
}
