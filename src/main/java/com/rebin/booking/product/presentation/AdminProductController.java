package com.rebin.booking.product.presentation;

import com.rebin.booking.product.dto.request.ProductCreateRequest;
import com.rebin.booking.product.dto.response.AdminProductResponse;
import com.rebin.booking.product.dto.response.ProductCreateResponse;
import com.rebin.booking.product.service.AdminProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/products")
public class AdminProductController {
    private final AdminProductService adminProductService;

    @Operation(summary = "상품 생성")
    @PostMapping
    public ResponseEntity<ProductCreateResponse> createProduct(@RequestBody @Valid ProductCreateRequest request) {
        return ResponseEntity.ok(adminProductService.createProduct(request));
    }

    @Operation(summary = "상품 수정")
    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long productId,
                                              @RequestBody @Valid ProductCreateRequest request) {
        adminProductService.updateProduct(productId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "상품 삭제")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        adminProductService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "상품 조회")
    @GetMapping
    public ResponseEntity<List<AdminProductResponse>> getProducts() {
        return ResponseEntity.ok(adminProductService.getProducts());
    }
}
