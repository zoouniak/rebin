package com.rebin.booking.reservation.service;

import com.rebin.booking.common.excpetion.ErrorCode;
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
import org.junit.jupiter.api.DisplayName;
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

import static com.rebin.booking.common.excpetion.ErrorCode.*;
import static com.rebin.booking.reservation.domain.type.ReservationStatusType.*;
import static com.rebin.booking.reservation.dto.request.ReservationLookUpRequest.AFTER;
import static com.rebin.booking.reservation.dto.request.ReservationLookUpRequest.BEFORE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    @DisplayName("예약한다")
    void saveNotice() {
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
        ReservationSaveResponse actual = reservationService.reserve(1L, req);

        // then
        assertThat(actual.code()).isEqualTo(reservationCode());
        Mockito.verify(reservationRepository, times(1)).save(any());
    }


    @Test
    @DisplayName("타임슬롯이_마감됐으면_예약_불가능하다")
    void saveNotice_unAvailableTimeSlot() {
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
        assertThatThrownBy(() -> reservationService.reserve(1L, req))
                .isInstanceOf(ReservationException.class)
                .hasMessage(RESERVATION_FULL.getMsg());

    }

    @Test
    @DisplayName("촬영전_예약을_조회한다")
    void getReservationByStatus_BeforeShooting() {
        // given
        Reservation reservation = reservation(PENDING_PAYMENT);
        ReservationLookUpRequest req = BEFORE;
        when(reservationFinders.mapping(req)).thenReturn(new ShootBeforeReservationFinder(reservationRepository));
        when(reservationRepository.findAllBeforeShootByMemberId(any())).thenReturn(List.of(reservation));
        // when
        List<ReservationResponse> actual = reservationService.getReservationsByStatus(reservation.getMember().getId(), req);

        // then
        assertThat(actual.size()).isEqualTo(1);
        verify(reservationRepository, times(1)).findAllBeforeShootByMemberId(any());
    }

    @Test
    @DisplayName("촬영후_예약을_조회한다")
    void getReservationByStatus_AfterShooting() {
        // given
        Reservation reservation = reservation(SHOOTING_COMPLETED);
        ReservationLookUpRequest req = AFTER;
        when(reservationFinders.mapping(req)).thenReturn(new ShootAfterReservationFinder(reservationRepository));
        when(reservationRepository.findAllAfterShootByMemberId(any())).thenReturn(List.of(reservation));
        // when
        List<ReservationResponse> actual = reservationService.getReservationsByStatus(reservation.getMember().getId(), req);

        // then
        assertThat(actual.size()).isEqualTo(1);
        verify(reservationRepository, times(1)).findAllAfterShootByMemberId(any());
    }

    @Test
    @DisplayName("촬영취소_예약을_조회한다")
    void getReservationByStatus_CanceledShooting() {
        // given
        Reservation reservation = reservation(CANCELED);
        ReservationLookUpRequest req = ReservationLookUpRequest.CANCELED;
        when(reservationFinders.mapping(req)).thenReturn(new ShootCanceledReservationFinder(reservationRepository));
        when(reservationRepository.findAllCanceledShootByMemberId(any())).thenReturn(List.of(reservation));
        // when
        List<ReservationResponse> actual = reservationService.getReservationsByStatus(reservation.getMember().getId(), req);

        // then
        assertThat(actual.size()).isEqualTo(1);
        verify(reservationRepository, times(1)).findAllCanceledShootByMemberId(any());
    }

    @Test
    @DisplayName("예약_상세를_조회한다")
    void getReservationDetail() {
        // given
        Reservation reservation = reservation(PENDING_PAYMENT);
        when(reservationRepository.existsByMemberIdAndId(any(), any())).thenReturn(true);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));
        // when
        ReservationDetailResponse actual = reservationService.getReservationDetail(reservation.getMember().getId(), 1L);

        // then
        assertThat(actual.code()).isEqualTo(reservation.getCode());
        verify(reservationRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("잘못된예약번호로_조회하면_에러가_발생한다")
    void getReservationDetail_invalidReservationId() {
        // given
        Reservation reservation = reservation(PENDING_PAYMENT);
        when(reservationRepository.existsByMemberIdAndId(any(), any())).thenReturn(false);

        // when
        // then
        assertThatThrownBy(() -> reservationService.getReservationDetail(reservation.getMember().getId(), 1L))
                .isInstanceOf(ReservationException.class)
                .hasMessage(INVALID_RESERVATION.getMsg());
        verify(reservationRepository, times(0)).findById(any());
    }

    @Test
    @DisplayName(" 예약을_취소한다")
    void cancelReservation() {
        Reservation reservation = reservation(PENDING_PAYMENT);
        when(reservationRepository.existsByMemberIdAndId(any(), any())).thenReturn(true);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));
        when(cancelValidator.canCancelReservation(any())).thenReturn(true);

        reservationService.cancelReservation(1L, 1L);

        assertThat(reservation.getStatus()).isEqualTo(CANCELED);
        assertThat(reservation.getTimeSlot().isAvailable()).isTrue();
    }

    @Test
    @DisplayName("취소가_불가능한_예약을_취소한다")
    void cancelReservation_unAvailableCancel() {
        Reservation reservation = reservation(SHOOTING_COMPLETED);
        when(reservationRepository.existsByMemberIdAndId(any(), any())).thenReturn(true);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));
        when(cancelValidator.canCancelReservation(any())).thenReturn(false);

        assertThatThrownBy(() -> reservationService.cancelReservation(1L, 1L))
                .isInstanceOf(ReservationException.class)
                .hasMessage(CANT_CANCEL.getMsg());

    }

    @Test
    @DisplayName("예약금_입금확인_요청을_보낸다")
    void requestPaymentConfirmation() {
        Reservation reservation = reservation(PENDING_PAYMENT);
        when(reservationRepository.existsByMemberIdAndIdAndStatusIn(1L, 1L, List.of(PENDING_PAYMENT))).thenReturn(true);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));
        ConfirmRequest request = new ConfirmRequest("오주은", LocalDate.of(2024, 4, 3));

        reservationService.requestPaymentConfirmation(1L, 1L, request);

        assertThat(reservation.getStatus()).isEqualTo(CONFIRM_REQUESTED);
    }

    @Test
    @DisplayName("타임슬롯을 변경한다")
    void rescheduleTimeSlot() {
        // given
        TimeSlot oldTimeSlot = new TimeSlot(LocalDate.of(2024, 12, 16), LocalTime.of(9, 0));
        Reservation reservation = Reservation.builder()
                .product(product())
                .member(member())
                .timeSlot(oldTimeSlot)
                .code(reservationCode())
                .status(PENDING_PAYMENT)
                .shootDate(oldTimeSlot.getDate())
                .isAgreeUpload(true)
                .isAgreePrivacyPolicy(true)
                .peopleCnt(2)
                .notes("note")
                .price(10_000)
                .canChange(true)
                .build();
        when(reservationRepository.existsByMemberIdAndIdAndStatusIn(1L, 1L, List.of(PENDING_PAYMENT
                , CONFIRM_REQUESTED
                , PAYMENT_CONFIRMED))).thenReturn(true);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));

        TimeSlot newTimeSlot = new TimeSlot(LocalDate.of(2024, 12, 26), LocalTime.of(10, 0));
        when(timeSlotRepository.findById(2L)).thenReturn(Optional.of(newTimeSlot));
        // when
        reservationService.rescheduleTimeSlot(1L, 1L, 2L);

        // then
        assertThat(newTimeSlot.isAvailable()).isFalse();
        assertThat(oldTimeSlot.isAvailable()).isTrue();
        assertThat(reservation.getTimeSlot()).isEqualTo(newTimeSlot);
    }

    @Test
    @DisplayName("타임슬롯을 변경할 수 없는 예약의 타임슬롯을 변경한다.")
    void rescheduleTimeSlot_cantChange() {
        TimeSlot oldTimeSlot = new TimeSlot(LocalDate.of(2024, 12, 16), LocalTime.of(9, 0));
        Reservation reservation = Reservation.builder()
                .product(product())
                .member(member())
                .timeSlot(oldTimeSlot)
                .code(reservationCode())
                .status(PENDING_PAYMENT)
                .shootDate(oldTimeSlot.getDate())
                .isAgreeUpload(true)
                .isAgreePrivacyPolicy(true)
                .peopleCnt(2)
                .notes("note")
                .price(10_000)
                .canChange(false)
                .build();
        when(reservationRepository.existsByMemberIdAndIdAndStatusIn(1L, 1L, List.of(PENDING_PAYMENT
                , CONFIRM_REQUESTED
                , PAYMENT_CONFIRMED))).thenReturn(true);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.rescheduleTimeSlot(1L, 1L, 2L))
                .isInstanceOf(ReservationException.class)
                .hasMessage(ErrorCode.CANCELLATION_NOT_ALLOWED.getMsg());
    }

    @Test
    @DisplayName("촬영 당일에는 타임슬롯을 변경할 수 없다.")
    void rescheduleTimeSlot_nowShootingDate() {
        TimeSlot oldTimeSlot = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0));
        Reservation reservation = Reservation.builder()
                .product(product())
                .member(member())
                .timeSlot(oldTimeSlot)
                .code(reservationCode())
                .status(PENDING_PAYMENT)
                .shootDate(oldTimeSlot.getDate())
                .isAgreeUpload(true)
                .isAgreePrivacyPolicy(true)
                .peopleCnt(2)
                .notes("note")
                .price(10_000)
                .canChange(true)
                .build();
        when(reservationRepository.existsByMemberIdAndIdAndStatusIn(1L, 1L, List.of(PENDING_PAYMENT
                , CONFIRM_REQUESTED
                , PAYMENT_CONFIRMED))).thenReturn(true);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.rescheduleTimeSlot(1L, 1L, 2L))
                .isInstanceOf(ReservationException.class)
                .hasMessage(ErrorCode.CANCELLATION_NOT_ALLOWED.getMsg());
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
        return new Member("loginId", "email", "nickname", ProviderType.KAKAO);
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
                .canChange(true)
                .build();
    }

    private static String reservationCode() {
        return "RE2410161131ABC";
    }

}