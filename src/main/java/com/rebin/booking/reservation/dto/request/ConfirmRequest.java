package com.rebin.booking.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record ConfirmRequest(
        @NotNull(message = "입금자명은 필수값입니다.")
        String payerName,

        @PastOrPresent(message = "입금날짜는 미래일 수 없습니다.")
        LocalDate paymentDate
) {
}
