package com.rebin.booking.review.dto.request;

import jakarta.validation.constraints.Size;

public record ReviewEditRequest(
        @Size(min = 10, max = 500, message = "리뷰 길이는 10자 이상, 500자 이하여야 합니다.")
        String content
) {
}
