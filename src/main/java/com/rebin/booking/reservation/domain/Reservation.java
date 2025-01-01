package com.rebin.booking.reservation.domain;

import com.rebin.booking.common.domain.BaseTimeEntity;
import com.rebin.booking.common.excpetion.ReservationException;
import com.rebin.booking.member.domain.Member;
import com.rebin.booking.product.domain.Product;
import com.rebin.booking.reservation.domain.type.ReservationStatusType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.rebin.booking.common.excpetion.ErrorCode.CONFIRM_REQUEST_NOT_ALLOWED;
import static com.rebin.booking.common.excpetion.ErrorCode.REVIEW_NOT_ALLOWED;
import static com.rebin.booking.reservation.domain.type.ReservationStatusType.*;
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

    @Column
    private boolean canChange;

    @Column
    private String payerName;

    @Column
    private LocalDate paymentDate;

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
        // 타임슬롯 의존성 제거
        this.timeSlot = null;
    }

    public void sendConfirmRequest(String payerName, LocalDate paymentDate) {
        this.status = CONFIRM_REQUESTED;
        this.payerName = payerName;
        this.paymentDate = paymentDate;
    }

    public void confirm() {
        if (this.status != CONFIRM_REQUESTED)
            throw new ReservationException(CONFIRM_REQUEST_NOT_ALLOWED);
        this.status = PAYMENT_CONFIRMED;
    }

    public void changeTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
        this.canChange = false;
        // 촬영 날짜 동기화
        this.shootDate = timeSlot.getDate();
    }

    public void changeStatusAfterReview() {
        if(this.status != SHOOTING_COMPLETED)
            throw new ReservationException(REVIEW_NOT_ALLOWED);
        this.status = REVIEW_COMPLETED;
    }
}
