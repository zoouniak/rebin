package com.rebin.booking.product.service;

import com.rebin.booking.product.domain.repository.CustomProductRepository;
import com.rebin.booking.product.domain.repository.ProductRepository;
import com.rebin.booking.product.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CustomProductRepository customProductRepository;

    public List<ProductResponse> getProducts(final Long productId) {
        return customProductRepository.getProducts(productId);
    }

}
