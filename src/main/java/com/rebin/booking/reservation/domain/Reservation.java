package com.rebin.booking.reservation.domain;

import com.rebin.booking.common.domain.BaseTimeEntity;
import com.rebin.booking.member.domain.Member;
import com.rebin.booking.product.domain.Product;
import com.rebin.booking.reservation.domain.type.ReservationStatusType;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
public class Reservation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "time_slot_id")
    private TimeSlot timeSlot;

    @Column(unique = true, updatable = false, length = 15)
    private String code;

    @Column
    @Enumerated(value = STRING)
    private ReservationStatusType status;

    @Column
    private boolean isAgreeUpload;

    @Column
    private int peopleCnt;

    @Column(length = 2000)
    private String notes;

    @Column
    private int price;


}
