package com.rebin.booking.reservation.domain;

import com.rebin.booking.common.domain.BaseTimeEntity;
import com.rebin.booking.member.domain.Member;
import com.rebin.booking.product.domain.Product;
import com.rebin.booking.reservation.domain.type.ReservationStatusType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.rebin.booking.reservation.domain.type.ReservationStatusType.CANCELED;
import static com.rebin.booking.reservation.domain.type.ReservationStatusType.CONFIRM_REQUESTED;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private LocalDate shootDate;

    @Column
    private boolean isAgreeUpload;

    @Column
    private boolean isAgreePrivacyPolicy;

    @Column
    private int peopleCnt;

    @Column(length = 2000)
    private String notes;

    @Column
    private int price;

    private boolean canChange;

    @Builder
    public Reservation(Product product, Member member, TimeSlot timeSlot, String code, ReservationStatusType status, LocalDate shootDate, boolean isAgreeUpload, boolean isAgreePrivacyPolicy, int peopleCnt, String notes, int price, boolean canChange) {
        this.product = product;
        this.member = member;
        this.timeSlot = timeSlot;
        this.code = code;
        this.status = status;
        this.shootDate = shootDate;
        this.isAgreeUpload = isAgreeUpload;
        this.isAgreePrivacyPolicy = isAgreePrivacyPolicy;
        this.peopleCnt = peopleCnt;
        this.notes = notes;
        this.price = price;
        this.canChange = canChange;
    }

    public void cancel() {
        this.status = CANCELED;
    }

    public void sendConfirmRequest() {
        this.status = CONFIRM_REQUESTED;
    }

    public void changeTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
        this.canChange = false;
    }
}
