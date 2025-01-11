package com.rebin.booking.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rebin.booking.reservation.domain.Reservation;
import com.rebin.booking.reservation.domain.type.ReservationStatusType;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        String productName,
        String productImage,
        int productPrice,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate shootDate,
        ReservationStatusType statusType

) {
    public static ReservationResponse of(Reservation reservation){
        return new ReservationResponse(
                reservation.getId(),
                reservation.getProduct().getName(),
                reservation.getProduct().getThumbnail(),
                reservation.getProduct().getPrice(),
                reservation.getShootDate(),
                reservation.getStatus()
        );
    }
}
