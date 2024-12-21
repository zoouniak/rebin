package com.rebin.booking.reservation.dto.response;

import com.rebin.booking.product.dto.response.ProductResponse;
import com.rebin.booking.reservation.domain.Reservation;
import com.rebin.booking.reservation.domain.type.ReservationStatusType;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationDetailResponse(
        Long id,
        String code,
        LocalDate shootDate,
        LocalTime time,
        String name,
        String phone,
        ReservationStatusType status,
        int peopleCnt,
        boolean isAgreeUpload,
        String notes,
        int price,
        boolean canChange,
        ProductResponse product

) {
    public static ReservationDetailResponse of(Reservation reservation) {
        return new ReservationDetailResponse(
                reservation.getId(),
                reservation.getCode(),
                reservation.getShootDate(),
                reservation.getTimeSlot().getStartTime(),
                reservation.getMember().getName(),
                reservation.getMember().getPhone(),
                reservation.getStatus(),
                reservation.getPeopleCnt(),
                reservation.isAgreeUpload(),
                reservation.getNotes(),
                reservation.getPrice(),
                reservation.isCanChange(),
                ProductResponse.of(reservation.getProduct())
        );
    }
}
