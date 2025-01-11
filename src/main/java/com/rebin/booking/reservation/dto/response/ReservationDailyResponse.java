package com.rebin.booking.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record ReservationDailyResponse(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        int count,

        List<ReservationSummaryResponse> reservation
) {
}
