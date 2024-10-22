package com.rebin.booking.reservation.validator;

import com.rebin.booking.reservation.domain.Reservation;

import java.time.LocalDate;

import static com.rebin.booking.reservation.domain.type.ReservationStatusType.CANCELED;
import static com.rebin.booking.reservation.domain.type.ReservationStatusType.COMPLETED;

public class ReservationCancelValidator {
    private static final int CANCEL_DAYS_LIMIT = 5;

    public static boolean canCancelReservation(Reservation reservation) {
        return isReservationCancelableByStatus(reservation) && isReservationCancelableByDate(reservation);
    }

    // 상태 확인
    private static boolean isReservationCancelableByStatus(Reservation reservation) {
        return reservation.getStatus() != COMPLETED && reservation.getStatus() != CANCELED;
    }

    // 날짜 확인
    private static boolean isReservationCancelableByDate(Reservation reservation) {
        LocalDate now = LocalDate.now();
        return now.isBefore(reservation.getShootDate().minusDays(CANCEL_DAYS_LIMIT));
    }

}
