package com.rebin.booking.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLIke {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long member;

    private Long product;

    public ProductLIke(Long member, Long product) {
        this.member = member;
        this.product = product;
    }
}
