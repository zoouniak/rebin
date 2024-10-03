package com.rebin.booking.product.dto.response;

public record ProductResponse(
        Long id,
        String name,
        String thumbnail,
        String summary,
        int price
) {
}
