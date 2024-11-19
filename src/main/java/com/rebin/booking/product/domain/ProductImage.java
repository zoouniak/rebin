package com.rebin.booking.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImage {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String url;

    @Builder
    public ProductImage(Product product, String url) {
        this.product = product;
        this.url = url;
    }
    public ProductImage(String url){
        this(null, url);
    }

    public void setProduct(Product product){
        this.product = product;
    }
}
