package com.rebin.booking.product.presentation;

import com.rebin.booking.auth.domain.Accessor;
import com.rebin.booking.auth.domain.Auth;
import com.rebin.booking.product.dto.response.ProductDetailResponse;
import com.rebin.booking.product.dto.response.ProductResponse;
import com.rebin.booking.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("")
    public ResponseEntity<List<ProductResponse>> getProducts(@RequestParam(name = "id", required = false) final Long id) {
        return ResponseEntity.ok(productService.getProducts(id));
    }

    @GetMapping("/{id}")
    ResponseEntity<ProductDetailResponse> getProduct(@Auth final Accessor accessor, @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(productService.getProductDetail(id, accessor.getMemberId()));
    }
}
