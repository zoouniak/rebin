package com.rebin.booking.product.dto.response;

import com.rebin.booking.product.domain.Product;

import java.util.List;

public record ProductDetailResponse(
        Long id,
        String name,
        String thumbnail,
        String summary,
        int price,
        String description,
        List<ProductImageResponse> images,
        String guideLine,
        int deposit,
        int additionalFee

) {
    public static ProductDetailResponse of(Product product) {
        List<ProductImageResponse> imageResponses = product.getImages()
                .stream()
                .map(ProductImageResponse::of)
                .toList();

        return new ProductDetailResponse(
                product.getId(),
                product.getName(),
                product.getThumbnail(),
                product.getSummary(),
                product.getPrice(),
                product.getDescription(),
                imageResponses,
                product.getGuideLine(),
                product.getDeposit(),
                product.getAdditionalFee()
        );
    }
}
