package com.rebin.booking.product.dto.response;

import com.rebin.booking.product.domain.ProductImage;

public record ProductImageResponse(
        Long id,
        String url
) {
    public static ProductImageResponse of(ProductImage image){
        return new ProductImageResponse(
                image.getId(),
                image.getUrl()
        );
    }
}
