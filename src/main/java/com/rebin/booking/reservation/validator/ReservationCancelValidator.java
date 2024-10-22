package com.rebin.booking.reservation.validator;

import java.time.LocalDate;

public class ReservationCancelValidator {
    private static final int CANCEL_DAYS_LIMIT = 5;
    public static boolean canCancelReservation(LocalDate shootDate){
        LocalDate now = LocalDate.now();
        return now.isBefore(shootDate.minusDays(5));
    }
}
