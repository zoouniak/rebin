package com.rebin.booking.reservation.dto.response;

import com.rebin.booking.reservation.domain.type.ReservationStatusType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record AdminReservationResponse(
        Long id,
        String code,
        String productName,
        String name,
        LocalDateTime shootDateTime,
        LocalDateTime createdAt,
        ReservationStatusType status
) {
    public AdminReservationResponse(Long id, String code, String productName, String name, LocalDate date, LocalTime time, LocalDateTime reserveTime, ReservationStatusType status) {
        this(id, code, productName, name, date.atTime(time), reserveTime, status);
    }
}
