package com.rebin.booking.product.dto.response;

import com.rebin.booking.product.domain.Product;

import java.util.List;
import java.util.stream.Collectors;

public record ProductDetailResponse(
        Long id,
        String description,
        List<ProductImageResponse> images,
        String guideLine,
        int deposit,
        int additionalFee,
        boolean isMemberLike

) {
    public static ProductDetailResponse of(Product product, boolean isMemberLike) {
        List<ProductImageResponse> imageResponses = product.getImages()
                .stream()
                .map(ProductImageResponse::of)
                .collect(Collectors.toList());

        return new ProductDetailResponse(
                product.getId(),
                product.getDescription(),
                imageResponses,
                product.getGuideLine(),
                product.getDeposit(),
                product.getAdditionalFee(),
                isMemberLike
        );
    }
}
