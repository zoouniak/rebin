package com.rebin.booking.product.dto.response;

public record AdminProductResponse(
        Long id,
        String name,
        int price,
        int totalReservationCount
) {
}
