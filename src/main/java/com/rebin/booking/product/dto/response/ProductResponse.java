package com.rebin.booking.product.dto.response;

import com.rebin.booking.product.domain.Product;

public record ProductResponse(
        Long id,
        String name,
        String thumbnail,
        String summary,
        int price
) {
    public static ProductResponse of(Product product){
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getThumbnail(),
                product.getSummary(),
                product.getPrice()
        );
    }
}
