package com.rebin.booking.product.service;

import com.rebin.booking.common.excpetion.ProductException;
import com.rebin.booking.product.domain.Product;
import com.rebin.booking.product.domain.repository.CustomProductRepository;
import com.rebin.booking.product.domain.repository.ProductRepository;
import com.rebin.booking.product.dto.response.ProductDetailResponse;
import com.rebin.booking.product.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rebin.booking.common.excpetion.ErrorCode.INVALID_PRODUCT;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    private final CustomProductRepository customProductRepository;

    public List<ProductResponse> getProducts(final Long productId) {
        return customProductRepository.getProducts(productId);
    }

    public ProductDetailResponse getProductDetail(final Long productId) {
        return ProductDetailResponse.of(getProduct(productId));
    }

    private Product getProduct(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(INVALID_PRODUCT));
    }
}
