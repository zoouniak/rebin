package com.rebin.booking.review.dto.response;

import com.rebin.booking.product.dto.response.ProductResponse;

public record ReviewWithProductResponse(
        ReviewResponse reviewResponse,
        ProductResponse productResponse
) {
}
