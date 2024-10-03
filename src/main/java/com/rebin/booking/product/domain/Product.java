package com.rebin.booking.product.domain;

import com.rebin.booking.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(length = 60)
    private String summary;

    @Column(length = 5000)
    private String description;

    @Column
    private String thumbnail;

    @Column
    private int extraPersonFee;

    @Column(length = 5000)
    private String guildLine;
}
