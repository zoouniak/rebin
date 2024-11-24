package com.rebin.booking.review.dto.response;

import java.util.List;

public record ReviewDetailResponse(
        ReviewResponse reviewResponse,
        List<CommentResponse> commentResponses
) {
}
