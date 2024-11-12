package com.rebin.booking.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReviewCreateRequest(
        @NotNull
        Long reservationId,
        @Min(value=10, message = "리뷰 길이는 10자이상이여야 합니다.")
        @Max(value = 500, message = "리뷰 길이는 500자이하여야 합니다.")
        String content
) {
}
