package com.rebin.booking.reservation.service;

import com.rebin.booking.common.excpetion.ReservationException;
import com.rebin.booking.member.domain.Member;
import com.rebin.booking.member.domain.repository.MemberRepository;
import com.rebin.booking.product.domain.Product;
import com.rebin.booking.product.domain.repository.ProductRepository;
import com.rebin.booking.reservation.domain.Reservation;
import com.rebin.booking.reservation.domain.TimeSlot;
import com.rebin.booking.reservation.domain.repository.ReservationRepository;
import com.rebin.booking.reservation.domain.repository.TimeSlotRepository;
import com.rebin.booking.reservation.dto.request.ReservationLookUpRequest;
import com.rebin.booking.reservation.dto.request.ReservationRequest;
import com.rebin.booking.reservation.dto.response.ReservationDetailResponse;
import com.rebin.booking.reservation.dto.response.ReservationResponse;
import com.rebin.booking.reservation.dto.response.ReservationSaveResponse;
import com.rebin.booking.reservation.service.strategy.ReservationFinder;
import com.rebin.booking.reservation.service.strategy.ReservationFinders;
import com.rebin.booking.reservation.util.ReservationCodeGenerator;
import com.rebin.booking.reservation.validator.ReservationCancelValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.rebin.booking.common.excpetion.ErrorCode.*;
import static com.rebin.booking.reservation.domain.type.ReservationStatusType.PENDING_PAYMENT;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ProductRepository productRepository;
    private final ReservationCodeService reservationCodeService;
    private final ReservationFinders reservationFinders;

    private static final int ATTEMPT_CNT = 10;

    @Transactional
    public ReservationSaveResponse reserve(final Long memberId, final ReservationRequest request) {
        Member member = findMemberWithIdAndEmail(memberId, request.email());
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
                .price(request.price())
                .build();

        Reservation save = reservationRepository.save(reservation);
        return new ReservationSaveResponse(save.getCode());
    }

    public List<ReservationResponse> getReservationsByStatus(final Long memberId, final ReservationLookUpRequest status) {
        ReservationFinder strategy = reservationFinders.mapping(status);
        return strategy.getReservations(memberId);
    }

    public ReservationDetailResponse getReservationDetail(final Long memberId, final Long reservationId) {
        validReservationWithMember(memberId, reservationId);
        return ReservationDetailResponse.of(findReservation(reservationId));
    }

    @Transactional
    public void cancelReservation(Long memberId, Long reservationId) {
        validReservationWithMember(memberId, reservationId);

        Reservation reservation = findReservation(reservationId);
        if(!ReservationCancelValidator.canCancelReservation(reservation.getShootDate()))
            throw new ReservationException(CANT_CANCEL);

        reservation.cancel();
        reservation.getTimeSlot().cancel();
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


}
