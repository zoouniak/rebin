package com.rebin.booking.reservation.service;

import com.rebin.booking.common.excpetion.ReservationException;
import com.rebin.booking.member.domain.Member;
import com.rebin.booking.member.domain.repository.MemberRepository;
import com.rebin.booking.member.type.ProviderType;
import com.rebin.booking.product.domain.Product;
import com.rebin.booking.product.domain.repository.ProductRepository;
import com.rebin.booking.reservation.domain.Reservation;
import com.rebin.booking.reservation.domain.TimeSlot;
import com.rebin.booking.reservation.domain.repository.ReservationRepository;
import com.rebin.booking.reservation.domain.repository.TimeSlotRepository;
import com.rebin.booking.reservation.domain.type.ReservationStatusType;
import com.rebin.booking.reservation.dto.request.ReservationRequest;
import com.rebin.booking.reservation.dto.response.ReservationResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @InjectMocks
    ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private TimeSlotRepository timeSlotRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ReservationCodeService reservationCodeService;

    @Test
    void 예약_한다() {
        // given
        Member member = member();
        Product product = product();
        TimeSlot timeSlot = timeSlot();

        when(memberRepository.findByIdAndEmail(any(), any())).thenReturn(Optional.of(member));
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(timeSlotRepository.findById(any())).thenReturn(Optional.of(timeSlot));
        when(reservationCodeService.isCodeUnique(any())).thenReturn(true);

        ReservationRequest req = reservationRequest();
        Reservation reservation = reservation();

        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        // when
        ReservationResponse reserve = reservationService.reserve(1L, req);

        // then
        Assertions.assertEquals(reservationCode(), reserve.code());
        Mockito.verify(reservationRepository, times(1)).save(any());
    }


    @Test
    void 타임슬롯이_마감됐으면_예약_불가능하다() {
        Member member = member();
        Product product = product();
        TimeSlot timeSlot = timeSlot();
        timeSlot.SetUnAvailable();

        when(memberRepository.findByIdAndEmail(any(), any())).thenReturn(Optional.of(member));
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(timeSlotRepository.findById(any())).thenReturn(Optional.of(timeSlot));
        when(reservationCodeService.isCodeUnique(any())).thenReturn(true);

        ReservationRequest req = reservationRequest();

        // when, then
        ReservationException exception = assertThrows(ReservationException.class, () -> reservationService.reserve(1L, req));
        Assertions.assertEquals("R002", exception.getCode());
    }

    private static ReservationRequest reservationRequest() {
        return new ReservationRequest(
                "name",
                "email",
                "01012345678",
                2,
                "notes",
                true,
                true,
                1L,
                1L,
                10000
        );
    }

    private static TimeSlot timeSlot() {
        return new TimeSlot(LocalDate.now(), LocalTime.now());
    }

    private static Product product() {
        return Product.builder()
                .id(1L)
                .description("설명")
                .extraPersonFee(10_000)
                .guideLine("가이드라인")
                .name("프로필 사진")
                .price(70_000)
                .summary("요약")
                .thumbnail("썸네일 경로")
                .build();
    }


    private static Member member() {
        return new Member("loginId", "email", ProviderType.KAKAO);
    }

    private static Reservation reservation() {
        ReservationRequest req = reservationRequest();
        return Reservation.builder()
                .product(product())
                .member(member())
                .timeSlot(timeSlot())
                .code(reservationCode())
                .status(ReservationStatusType.PENDING)
                .isAgreeUpload(req.agreeToInstaUpload())
                .isAgreePrivacyPolicy(req.agreeToPrivacyPolicy())
                .peopleCnt(req.peopleCnt())
                .notes(req.notes())
                .price(req.price())
                .build();
    }

    private static String reservationCode() {
        return "RE2410161131ABC";
    }

}