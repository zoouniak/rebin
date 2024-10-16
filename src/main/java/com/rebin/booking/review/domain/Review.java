package com.rebin.booking.review.domain;

import com.rebin.booking.common.domain.BaseTimeEntity;
import com.rebin.booking.member.domain.Member;
import com.rebin.booking.product.domain.Product;
import com.rebin.booking.reservation.domain.Reservation;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column
    private LocalDate shootDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(length = 5000)
    private String content;

    @OneToMany(mappedBy = "review", cascade = REMOVE)
    private List<Comment> comments;
}
