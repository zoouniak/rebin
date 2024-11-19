package com.rebin.booking.reservation.dto.request;

import jakarta.validation.constraints.NotNull;

public record ReservationUpdateRequest(
        @NotNull(message = "타임슬롯 id는 필수값입니다.")
        Long timeSlotId
) {
}
