package com.rebin.booking.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimeSlotResponseForAdmin(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime time,
        boolean isAvailable,
        String reservationCode
) {
}
