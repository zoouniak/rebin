package com.rebin.booking.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record ReservationSummaryResponse(
        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime time,
        String productName
) {
}
