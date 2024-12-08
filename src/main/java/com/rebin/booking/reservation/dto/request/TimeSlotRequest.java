package com.rebin.booking.reservation.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimeSlotRequest(
        LocalDate date,
        LocalTime time
) {
}
