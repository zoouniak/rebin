package com.rebin.booking.product.dto.response;

import com.rebin.booking.product.domain.Product;

public record ProductReservationResponse(
        Long id,
        String name,
        int price,
        String thumbnail,
        int deposit,
        int additionalFee

) {
    public static ProductReservationResponse of(Product product) {
        return new ProductReservationResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getThumbnail(),
                product.getDeposit(),
                product.getAdditionalFee()
        );
    }
}
