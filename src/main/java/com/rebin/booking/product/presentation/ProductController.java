package com.rebin.booking.product.presentation;

import com.rebin.booking.product.dto.response.ProductResponse;
import com.rebin.booking.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
