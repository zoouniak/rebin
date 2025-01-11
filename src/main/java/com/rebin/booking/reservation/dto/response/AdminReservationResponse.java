package com.rebin.booking.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rebin.booking.reservation.domain.type.ReservationStatusType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record AdminReservationResponse(
        Long id,
        String code,
        String productName,
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime shootDateTime,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,
        ReservationStatusType status
) {
    public AdminReservationResponse(Long id, String code, String productName, String name, LocalDate date, LocalTime time, LocalDateTime reserveTime, ReservationStatusType status) {
        this(id, code, productName, name, date.atTime(time), reserveTime, status);
    }
}
