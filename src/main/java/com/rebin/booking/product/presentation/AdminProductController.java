package com.rebin.booking.product.presentation;

import com.rebin.booking.product.dto.request.ProductCreateRequest;
import com.rebin.booking.product.dto.response.ProductCreateResponse;
import com.rebin.booking.product.service.ProductWriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/products")
public class AdminProductController {
    private final ProductWriteService productWriteService;

    /*
    상품 생성
     */
    @PostMapping
    public ResponseEntity<ProductCreateResponse> createProduct(@RequestBody @Valid ProductCreateRequest request) {
        return ResponseEntity.ok(productWriteService.createProduct(request));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long productId,
                                              @RequestBody @Valid ProductCreateRequest request) {
        productWriteService.updateProduct(productId, request);
        return ResponseEntity.noContent().build();
    }
}
