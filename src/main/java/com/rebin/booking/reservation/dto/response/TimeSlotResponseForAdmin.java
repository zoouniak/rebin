package com.rebin.booking.reservation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimeSlotResponseForAdmin(
        Long id,
        LocalDate date,
        LocalTime time,
        boolean isAvailable,
        String reservationCode
) {
}