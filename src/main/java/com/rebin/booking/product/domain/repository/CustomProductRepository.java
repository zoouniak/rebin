package com.rebin.booking.product.domain.repository;

import com.rebin.booking.product.domain.ProductImage;
import com.rebin.booking.product.dto.response.ProductResponse;

import java.util.List;

public interface CustomProductRepository {
    List<ProductResponse> getProducts(final Long productId);

    void saveAll(List<ProductImage> images);

    void deleteAll(List<ProductImage> images);
}
