package com.rebin.booking.product.service;

import com.rebin.booking.product.domain.Product;
import com.rebin.booking.product.domain.repository.ProductRepository;
import com.rebin.booking.product.dto.response.ProductDetailResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    void getProductDetail() {
        // given
        Product product = Product.builder()
                .id(1L)
                .description("설명")
                .additionalFee(10_000)
                .deposit(10_000)
                .guideLine("가이드라인")
                .name("프로필 사진")
                .price(70_000)
                .summary("요약")
                .thumbnail("썸네일 경로")
                .build();

        final Long productId = product.getId();

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // when
        ProductDetailResponse productDetail = productService.getProductDetail(productId);

        // then
        Assertions.assertEquals(product.getId(), productDetail.id());
    }

}