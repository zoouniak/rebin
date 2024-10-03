package com.rebin.booking.product.domain;

import com.rebin.booking.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
public class ProductLIke {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne(cascade = {REMOVE})
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(cascade = {REMOVE})
    @JoinColumn(name = "product_id")
    private Product product;
}
