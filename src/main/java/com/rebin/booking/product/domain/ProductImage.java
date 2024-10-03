package com.rebin.booking.product.domain;

import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
public class ProductImage {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(cascade = {REMOVE}, fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String url;
}
