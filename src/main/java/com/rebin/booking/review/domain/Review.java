package com.rebin.booking.review.domain;

import com.rebin.booking.common.domain.BaseTimeEntity;
import com.rebin.booking.member.domain.Member;
import com.rebin.booking.product.domain.Product;
import com.rebin.booking.reservation.domain.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column
    @ColumnDefault("0")
    private int helpCnt;

    @Column(length = 5000)
    private String content;
}
