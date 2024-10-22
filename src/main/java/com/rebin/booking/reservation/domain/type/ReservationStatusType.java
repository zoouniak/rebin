package com.rebin.booking.reservation.domain.type;

public enum ReservationStatusType {
    PENDING_PAYMENT,    // 예약금 입금 요청 전
    PAYMENT_REQUESTED,  // 입금 요청 보냄
    PAYMENT_CONFIRMED,  // 입금 완료
    COMPLETED, // 촬영 완료
    CANCELED            // 취소됨
}
