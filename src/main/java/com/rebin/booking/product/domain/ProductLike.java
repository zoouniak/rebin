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
public class ProductLike {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long memberId;

    private Long productId;

    public ProductLike(Long memberId, Long productId) {
        this.memberId = memberId;
        this.productId = productId;
    }
}
