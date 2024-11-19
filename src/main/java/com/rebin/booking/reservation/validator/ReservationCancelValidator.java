package com.rebin.booking.reservation.validator;

import com.rebin.booking.reservation.domain.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.rebin.booking.reservation.domain.type.ReservationStatusType.*;

@Component
public class ReservationCancelValidator {
    private static final int CANCEL_DAYS_LIMIT = 5;

    public boolean canCancelReservation(Reservation reservation) {
        return isReservationCancelableByStatus(reservation) && isReservationCancelableByDate(reservation);
    }

    // 상태 확인
    private boolean isReservationCancelableByStatus(Reservation reservation) {
        return reservation.getStatus() != SHOOTING_COMPLETED && reservation.getStatus() != CANCELED;
    }

    // 날짜 확인
    private boolean isReservationCancelableByDate(Reservation reservation) {
        LocalDate now = LocalDate.now();
        return now.isBefore(reservation.getShootDate().minusDays(CANCEL_DAYS_LIMIT));
    }
}
