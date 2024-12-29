package com.rebin.booking.review.service;

import com.rebin.booking.member.domain.Member;
import com.rebin.booking.product.domain.Product;
import com.rebin.booking.reservation.domain.Reservation;
import com.rebin.booking.reservation.domain.TimeSlot;
import com.rebin.booking.reservation.domain.type.ReservationStatusType;
import com.rebin.booking.review.domain.Comment;
import com.rebin.booking.review.domain.Review;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.rebin.booking.member.type.ProviderType.KAKAO;

public abstract class FixtureUtil {
    public static Member member(){
        return new Member("loginId", "email", "nickname", KAKAO);
    }
    public static Review review(){
        return new Review(reservation(),member(),"reviewContents",null);
    }

    public static Comment comment(final String content){
        return new Comment(1L,review(),member(),content);
    }
    public static Reservation reservation(){
        return Reservation.builder()
                .product(product())
                .member(member())
                .timeSlot(timeSlot())
                .code("RE20241223GED")
                .status(ReservationStatusType.PENDING_PAYMENT)
                .shootDate(timeSlot().getDate())
                .isAgreeUpload(true)
                .isAgreePrivacyPolicy(true)
                .peopleCnt(2)
                .notes("notes")
                .price(50_000)
                .canChange(true)
                .build();
    }
    private static TimeSlot timeSlot() {
        return new TimeSlot(LocalDate.now(), LocalTime.now());
    }

    private static Product product() {
        return Product.builder()
                .id(1L)
                .description("설명")
                .additionalFee(10_000)
                .deposit(10_000)
                .guideLine("가이드라인")
                .name("프로필 사진")
                .price(70_000)
                .summary("요약")
                .thumbnail("썸네일 경로")
                .build();
    }

}
