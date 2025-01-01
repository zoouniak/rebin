package com.rebin.booking.reservation.dto.response;

import com.rebin.booking.reservation.domain.type.ReservationStatusType;

import java.time.LocalDateTime;

public record AdminReservationResponse(
        Long id,
        String code,
        String productName,
        String name,
        LocalDateTime time,
        LocalDateTime reserveTime,
        ReservationStatusType status
) {
}
