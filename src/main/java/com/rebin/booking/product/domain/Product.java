package com.rebin.booking.product.domain;

import com.rebin.booking.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.REMOVE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
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
    private String guideLine;

    @OneToMany(mappedBy = "product", cascade = REMOVE)
    private List<ProductImage> images = new ArrayList<>();

    @Builder
    public Product(Long id, String name, int price, String summary, String description, String thumbnail, int extraPersonFee, String guideLine,List<ProductImage> images) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.summary = summary;
        this.description = description;
        this.thumbnail = thumbnail;
        this.extraPersonFee = extraPersonFee;
        this.guideLine = guideLine;
        this.images = images;
    }
}
