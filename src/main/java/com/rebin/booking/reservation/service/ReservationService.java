package com.rebin.booking.reservation.service;

import com.rebin.booking.common.excpetion.ReservationException;
import com.rebin.booking.member.domain.Member;
import com.rebin.booking.member.domain.repository.MemberRepository;
import com.rebin.booking.product.domain.Product;
import com.rebin.booking.product.domain.repository.ProductRepository;
import com.rebin.booking.reservation.domain.Reservation;
import com.rebin.booking.reservation.domain.ReservationEvent;
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
import com.rebin.booking.reservation.service.strategy.ReservationFinder;
import com.rebin.booking.reservation.service.strategy.ReservationFinders;
import com.rebin.booking.reservation.util.PriceCalculator;
import com.rebin.booking.reservation.util.ReservationCodeGenerator;
import com.rebin.booking.reservation.validator.ReservationCancelValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.rebin.booking.common.excpetion.ErrorCode.*;
import static com.rebin.booking.reservation.domain.type.ReservationStatusType.*;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ProductRepository productRepository;
    private final ReservationCodeService reservationCodeService;
    private final ReservationFinders reservationFinders;
    private final ReservationCancelValidator cancelValidator;
    private final PriceCalculator calculator;
    private final ApplicationEventPublisher publisher;

    private static final int ATTEMPT_CNT = 10;

    @Transactional
    public ReservationSaveResponse reserve(final Long memberId, final ReservationRequest request) {
        Member member = findMemberWithIdAndEmail(memberId, request.email());
        member.updateMemberIfNameOrPhoneMissing(request.name(), request.phone());

        Product product = findProduct(request.productId());
        TimeSlot timeSlot = findTimeSlot(request.timeSlotId());
        String code = generateUniqueReservationCode();

        timeSlot.SetUnAvailable();

        Reservation reservation = Reservation.builder()
                .product(product)
                .member(member)
                .timeSlot(timeSlot)
                .code(code)
                .status(PENDING_PAYMENT)
                .isAgreeUpload(request.agreeToInstaUpload())
                .isAgreePrivacyPolicy(request.agreeToPrivacyPolicy())
                .shootDate(timeSlot.getDate())
                .peopleCnt(request.peopleCnt())
                .notes(request.notes())
                .price(calculator.calculatePrice(product.getPrice(), request.peopleCnt(), product.getAdditionalFee()))
                .canChange(true)
                .build();

        Reservation save = reservationRepository.save(reservation);

        // 관리자에게 메일 전송
        publisher.publishEvent(new ReservationEvent(reservation.getStatus(), reservation.getCode()));
        return new ReservationSaveResponse(save.getCode());
    }

    /*
     * 촬영 전/중/후 예약 내역 조회하는 함수
     */
    public List<ReservationResponse> getReservationsByStatus(final Long memberId, final ReservationLookUpRequest status) {
        ReservationFinder strategy = reservationFinders.mapping(status);
        return strategy.getReservations(memberId);
    }

    public ReservationDetailResponse getReservationDetail(final Long memberId, final Long reservationId) {
        validReservationWithMember(memberId, reservationId);
        return ReservationDetailResponse.of(findReservation(reservationId));
    }

    @Transactional
    public void cancelReservation(final Long memberId, final Long reservationId) {
        validReservationWithMember(memberId, reservationId);

        Reservation reservation = findReservation(reservationId);
        // 취소 가능한 지 확인
        if (!cancelValidator.canCancelReservation(reservation))
            throw new ReservationException(CANT_CANCEL);

        reservation.cancel();
        reservation.getTimeSlot().SetAvailable();

        publisher.publishEvent(new ReservationEvent(reservation.getStatus(), reservation.getCode()));
    }

    /*
     * 입금 후 입금 확인 요청을 전송하는 함수
     */
    @Transactional
    public void requestPaymentConfirmation(final Long memberId, final Long reservationId, final ConfirmRequest request) {
        validReservationWithMemberAndStatus(memberId, reservationId, List.of(PENDING_PAYMENT));
        Reservation reservation = findReservation(reservationId);
        reservation.sendConfirmRequest(request.payerName(), request.paymentDate());

        publisher.publishEvent(new ReservationEvent(reservation.getStatus(), reservation.getCode()));
    }

    /*
     * 촬영날짜 재설정
     */
    @Transactional
    public void rescheduleTimeSlot(final Long memberId, final Long reservationId, final Long timeSlotId) {
        // 촬영 전 예약인지 확인
        validReservationWithMemberAndStatus(memberId, reservationId, List.of(PENDING_PAYMENT
                , CONFIRM_REQUESTED
                , PAYMENT_CONFIRMED));
        Reservation reservation = findReservation(reservationId);

        // 변경 가능한 지 확인
        if (!reservation.isCanChange() || LocalDate.now().isEqual(reservation.getShootDate())) // 촬영날짜당일에는 변경 불가
            throw new ReservationException(CANCELLATION_NOT_ALLOWED);

        TimeSlot timeSlot = findTimeSlot(timeSlotId);

        reservation.getTimeSlot().SetAvailable();
        reservation.changeTimeSlot(timeSlot);

        timeSlot.SetUnAvailable();
    }


    private String generateUniqueReservationCode() {
        String generateCode;
        for (int i = 0; i < ATTEMPT_CNT; i++) {
            generateCode = ReservationCodeGenerator.generateCode();

            if (reservationCodeService.isCodeUnique(generateCode)) {
                return generateCode;
            }
        }
        throw new ReservationException(MAX_ATTEMPTS_EXCEEDED);
    }

    private Member findMemberWithIdAndEmail(final Long memberId, final String email) {
        return memberRepository.findByIdAndEmail(memberId, email)
                .orElseThrow(() -> new ReservationException(INVALID_REQUEST));
    }

    private Product findProduct(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ReservationException(INVALID_PRODUCT));
    }

    private TimeSlot findTimeSlot(final Long timeSlotId) {
        return timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new ReservationException(INVALID_TIMESLOT));
    }

    private Reservation findReservation(final Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(INVALID_RESERVATION));
    }

    private void validReservationWithMember(final Long memberId, final Long reservationId) {
        if (!reservationRepository.existsByMemberIdAndId(memberId, reservationId)) {
            throw new ReservationException(INVALID_RESERVATION);
        }
    }

    private void validReservationWithMemberAndStatus(final Long memberId, final Long reservationId, List<ReservationStatusType> status) {
        if (!reservationRepository.existsByMemberIdAndIdAndStatusIn(memberId, reservationId, status)) {
            throw new ReservationException(INVALID_RESERVATION);
        }
    }

}
