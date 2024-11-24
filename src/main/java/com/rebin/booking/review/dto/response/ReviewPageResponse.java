package com.rebin.booking.review.dto.response;

import java.util.List;

public record ReviewPageResponse(
        List<ReviewResponse> reviews,
        boolean isLastPage
) {
}