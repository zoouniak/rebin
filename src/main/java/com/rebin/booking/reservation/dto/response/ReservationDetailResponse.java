package com.rebin.booking.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rebin.booking.product.dto.response.ProductReservationResponse;
import com.rebin.booking.reservation.domain.Reservation;
import com.rebin.booking.reservation.domain.type.ReservationStatusType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ReservationDetailResponse(
        Long id,
        String code,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate shootDate,
        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime time,
        String name,
        String phone,
        ReservationStatusType status,
        int peopleCnt,
        boolean isAgreeUpload,
        String notes,
        int totalPrice,
        boolean canChange,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,
        String payerName,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate paymentDate,
        ProductReservationResponse productInfo

) {
    private static LocalTime getTime(Reservation reservation) {
        if (reservation.getTimeSlot() == null)
            return null;

        return reservation.getTimeSlot().getStartTime();
    }

    public static ReservationDetailResponse of(Reservation reservation) {
        return new ReservationDetailResponse(
                reservation.getId(),
                reservation.getCode(),
                reservation.getShootDate(),
                getTime(reservation),
                reservation.getMember().getName(),
                reservation.getMember().getPhone(),
                reservation.getStatus(),
                reservation.getPeopleCnt(),
                reservation.isAgreeUpload(),
                reservation.getNotes(),
                reservation.getPrice(),
                reservation.isCanChange(),
                reservation.getCreatedAt(),
                reservation.getPayerName(),
                reservation.getPaymentDate(),
                ProductReservationResponse.of(reservation.getProduct())
        );
    }
}
