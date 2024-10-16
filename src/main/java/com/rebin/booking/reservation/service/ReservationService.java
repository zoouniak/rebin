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
import com.rebin.booking.reservation.domain.type.ReservationStatusType;
import com.rebin.booking.reservation.dto.request.ReservationRequest;
import com.rebin.booking.reservation.dto.response.ReservationResponse;
import com.rebin.booking.reservation.util.ReservationCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.rebin.booking.common.excpetion.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ProductRepository productRepository;
    private final ReservationCodeService reservationCodeService;

    private static final int ATTEMPT_CNT = 10;

    @Transactional
    public ReservationResponse reserve(Long memberId, ReservationRequest request) {
        Member member = findMemberWithIdAndEmail(memberId, request.email());
        Product product = findProduct(request.productId());
        TimeSlot timeSlot = findTimeSlot(request.timeSlotId());
        String code = generateUniqueReservationCode();

        Reservation reservation = Reservation.builder()
                .product(product)
                .member(member)
                .timeSlot(timeSlot)
                .code(code)
                .status(ReservationStatusType.PENDING)
                .isAgreeUpload(request.agreeToInstaUpload())
                .isAgreePrivacyPolicy(request.agreeToPrivacyPolicy())
                .peopleCnt(request.peopleCnt())
                .notes(request.notes())
                .price(request.price())
                .build();

        Reservation save = reservationRepository.save(reservation);
        return new ReservationResponse(save.getCode());
    }

    private String generateUniqueReservationCode() {
        String generateCode;
        for(int i=0;i<ATTEMPT_CNT;i++){
            generateCode = ReservationCodeGenerator.generateCode();

            if(reservationCodeService.isCodeUnique(generateCode)){
                return generateCode;
            }
        }
        throw new ReservationException(MAX_ATTEMPTS_EXCEEDED);
    }

    private Member findMemberWithIdAndEmail(Long memberId, String email) {
        return memberRepository.findByIdAndEmail(memberId, email)
                .orElseThrow(() -> new ReservationException(INVALID_REQUEST));
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ReservationException(INVALID_PRODUCT));
    }

    private TimeSlot findTimeSlot(Long timeSlotId) {
        return timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new ReservationException(INVALID_TIMESLOT));
    }
}
