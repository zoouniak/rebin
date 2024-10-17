package com.rebin.booking.reservation.dto.response;

import com.rebin.booking.reservation.domain.Reservation;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        String productName,
        String productImage,
        LocalDate shootDate

) {
    public static ReservationResponse of(Reservation reservation){
        return new ReservationResponse(
                reservation.getId(),
                reservation.getProduct().getName(),
                reservation.getProduct().getThumbnail(),
                reservation.getShootDate()
        );
    }
}
