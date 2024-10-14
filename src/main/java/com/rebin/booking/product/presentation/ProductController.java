package com.rebin.booking.product.presentation;

import com.rebin.booking.auth.domain.Accessor;
import com.rebin.booking.auth.domain.Auth;
import com.rebin.booking.product.dto.response.ProductDetailResponse;
import com.rebin.booking.product.dto.response.ProductResponse;
import com.rebin.booking.product.service.ProductService;
import com.rebin.booking.review.dto.response.ReviewResponse;
import com.rebin.booking.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ReviewService reviewService;

    @GetMapping("")
    public ResponseEntity<List<ProductResponse>> getProducts(@RequestParam(name = "productId", required = false) final Long productId) {
        return ResponseEntity.ok(productService.getProducts(productId));
    }

    @GetMapping("/{productId}")
    ResponseEntity<ProductDetailResponse> getProduct(@Auth final Accessor accessor, @PathVariable final Long productId) {
        return ResponseEntity.ok(productService.getProductDetail(productId, accessor.getMemberId()));
    }

    @GetMapping("/{productId}/reviews")
    ResponseEntity<List<ReviewResponse>> getProductReviews(@Auth final Accessor accessor,
                                                           @PathVariable final Long productId,
                                                           @PageableDefault(sort = "review.id", direction = DESC) final Pageable pageable) {
        return ResponseEntity.ok(reviewService.getReviewsByProduct(accessor.getMemberId(), productId, pageable));
    }
}
