package com.rebin.booking.product.domain.repository;

import com.rebin.booking.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
