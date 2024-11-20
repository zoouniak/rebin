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
import com.rebin.booking.reservation.dto.request.ConfirmRequest;
import com.rebin.booking.reservation.dto.request.ReservationLookUpRequest;
import com.rebin.booking.reservation.dto.request.ReservationRequest;
import com.rebin.booking.reservation.dto.response.ReservationDetailResponse;
import com.rebin.booking.reservation.dto.response.ReservationResponse;
import com.rebin.booking.reservation.dto.response.ReservationSaveResponse;
import com.rebin.booking.reservation.service.strategy.ReservationFinders;
import com.rebin.booking.reservation.service.strategy.ShootAfterReservationFinder;
import com.rebin.booking.reservation.service.strategy.ShootBeforeReservationFinder;
import com.rebin.booking.reservation.service.strategy.ShootCanceledReservationFinder;
import com.rebin.booking.reservation.util.PriceCalculator;
import com.rebin.booking.reservation.validator.ReservationCancelValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.rebin.booking.reservation.domain.type.ReservationStatusType.*;
import static com.rebin.booking.reservation.dto.request.ReservationLookUpRequest.AFTER;
import static com.rebin.booking.reservation.dto.request.ReservationLookUpRequest.BEFORE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    @Mock
    private ReservationFinders reservationFinders;
    @Mock
    private ReservationCancelValidator cancelValidator;
    @Mock
    private ApplicationEventPublisher publisher;
    @Mock
    private PriceCalculator calculator;


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
        Reservation reservation = reservation(PENDING_PAYMENT);

        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        // when
        ReservationSaveResponse reserve = reservationService.reserve(1L, req);

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

    @Test
    void 촬영전_예약을_조회한다() {
        // given
        Reservation reservation = reservation(PENDING_PAYMENT);
        ReservationLookUpRequest req = BEFORE;
        when(reservationFinders.mapping(req)).thenReturn(new ShootBeforeReservationFinder(reservationRepository));
        when(reservationRepository.findAllBeforeShootByMemberId(any())).thenReturn(List.of(reservation));
        // when
        List<ReservationResponse> reservations = reservationService.getReservationsByStatus(reservation.getMember().getId(), req);

        // then
        assertEquals(1, reservations.size());  // 반환된 예약 리스트 크기 확인
        verify(reservationRepository, times(1)).findAllBeforeShootByMemberId(any());
    }

    @Test
    void 촬영후_예약을_조회한다() {
        // given
        Reservation reservation = reservation(SHOOTING_COMPLETED);
        ReservationLookUpRequest req = AFTER;
        when(reservationFinders.mapping(req)).thenReturn(new ShootAfterReservationFinder(reservationRepository));
        when(reservationRepository.findAllAfterShootByMemberId(any())).thenReturn(List.of(reservation));
        // when
        List<ReservationResponse> reservations = reservationService.getReservationsByStatus(reservation.getMember().getId(), req);

        // then
        assertEquals(1, reservations.size());  // 반환된 예약 리스트 크기 확인
        verify(reservationRepository, times(1)).findAllAfterShootByMemberId(any());
    }

    @Test
    void 촬영취소_예약을_조회한다() {
        // given
        Reservation reservation = reservation(CANCELED);
        ReservationLookUpRequest req = ReservationLookUpRequest.CANCELED;
        when(reservationFinders.mapping(req)).thenReturn(new ShootCanceledReservationFinder(reservationRepository));
        when(reservationRepository.findAllCanceledShootByMemberId(any())).thenReturn(List.of(reservation));
        // when
        List<ReservationResponse> reservations = reservationService.getReservationsByStatus(reservation.getMember().getId(), req);

        // then
        assertEquals(1, reservations.size());  // 반환된 예약 리스트 크기 확인
        verify(reservationRepository, times(1)).findAllCanceledShootByMemberId(any());
    }

    @Test
    void 예약_상세를_조회한다() {
        // given
        Reservation reservation = reservation(PENDING_PAYMENT);
        when(reservationRepository.existsByMemberIdAndId(any(), any())).thenReturn(true);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));
        // when
        ReservationDetailResponse detail = reservationService.getReservationDetail(reservation.getMember().getId(), 1L);

        // then
        Assertions.assertEquals(reservation.getCode(), detail.code());
        verify(reservationRepository, times(1)).findById(any());
    }

    @Test
    void 잘못된예약번호로_조회하면_에러가_발생한다() {
        // given
        Reservation reservation = reservation(PENDING_PAYMENT);
        when(reservationRepository.existsByMemberIdAndId(any(), any())).thenReturn(false);

        // when
        ReservationException exception = assertThrows(ReservationException.class, () -> reservationService.getReservationDetail(reservation.getMember().getId(), 1L));

        // then
        Assertions.assertEquals("R004", exception.getCode());
        verify(reservationRepository, times(0)).findById(any());
    }

    @Test
    void 예약을_취소한다() {
        Reservation reservation = reservation(PENDING_PAYMENT);
        when(reservationRepository.existsByMemberIdAndId(any(), any())).thenReturn(true);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));
        when(cancelValidator.canCancelReservation(any())).thenReturn(true);

        reservationService.cancelReservation(1L, 1L);

        Assertions.assertEquals(CANCELED, reservation.getStatus());
        Assertions.assertTrue(reservation.getTimeSlot().isAvailable());
    }

    @Test
    void 취소가_불가능한_예약을_취소한다() {
        Reservation reservation = reservation(SHOOTING_COMPLETED);
        when(reservationRepository.existsByMemberIdAndId(any(), any())).thenReturn(true);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));
        when(cancelValidator.canCancelReservation(any())).thenReturn(false);

        ReservationException reservationException = assertThrows(ReservationException.class, () -> reservationService.cancelReservation(1L, 1L));

        Assertions.assertEquals("R005", reservationException.getCode());
    }

    @Test
    void 예약금_입금확인_요청을_보낸다() {
        Reservation reservation = reservation(PENDING_PAYMENT);
        when(reservationRepository.existsByMemberIdAndId(any(), any())).thenReturn(true);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));
        ConfirmRequest request = new ConfirmRequest("오주은",LocalDate.of(2024,4,3));

        reservationService.requestPaymentConfirmation(1L, 1L, request);

        Assertions.assertEquals(CONFIRM_REQUESTED, reservation.getStatus());
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
                1L
        );
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


    private static Member member() {
        return new Member("loginId", "email","nickname", ProviderType.KAKAO);
    }

    private static Reservation reservation(ReservationStatusType statusType) {
        ReservationRequest req = reservationRequest();
        TimeSlot timeSlot = timeSlot();
        return Reservation.builder()
                .product(product())
                .member(member())
                .timeSlot(timeSlot)
                .code(reservationCode())
                .status(statusType)
                .shootDate(timeSlot.getDate())
                .isAgreeUpload(req.agreeToInstaUpload())
                .isAgreePrivacyPolicy(req.agreeToPrivacyPolicy())
                .peopleCnt(req.peopleCnt())
                .notes(req.notes())
                .price(10_000)
                .build();
    }

    private static String reservationCode() {
        return "RE2410161131ABC";
    }

}