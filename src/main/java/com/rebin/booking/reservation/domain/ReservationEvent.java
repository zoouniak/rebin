package com.rebin.booking.reservation.domain;

import com.rebin.booking.reservation.domain.type.ReservationStatusType;

public record ReservationEvent(
        ReservationStatusType type,
        String code
) {
}
