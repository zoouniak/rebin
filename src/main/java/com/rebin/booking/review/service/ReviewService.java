package com.rebin.booking.review.service;

import com.rebin.booking.common.excpetion.ReviewException;
import com.rebin.booking.member.domain.Member;
import com.rebin.booking.member.domain.repository.MemberRepository;
import com.rebin.booking.reservation.domain.Reservation;
import com.rebin.booking.reservation.domain.repository.ReservationRepository;
import com.rebin.booking.review.domain.Review;
import com.rebin.booking.review.domain.repository.ReviewHelpRepository;
import com.rebin.booking.review.domain.repository.ReviewRepository;
import com.rebin.booking.review.dto.request.ReviewCreateRequest;
import com.rebin.booking.review.dto.response.ReviewCreateResponse;
import com.rebin.booking.review.dto.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.rebin.booking.common.excpetion.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewHelpRepository reviewHelpRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;

    public List<ReviewResponse> getReviewsByProduct(final Long memberId, final Long productId, final Pageable pageable) {
        return reviewRepository.findByProductIdAndPageable(productId, pageable).stream()
                .map(review -> {
                    int helpCnt = reviewHelpRepository.countByReviewId(review.getId());
                    boolean isHelped = reviewHelpRepository.existsByMemberIdAndReviewId(memberId, review.getId());
                    int commentCnt = review.getComments().size();
                    return new ReviewResponse(
                            review.getId(),
                            review.getMember().getNickname(),
                            review.getContent(),
                            review.getShootDate(),
                            helpCnt,
                            isHelped,
                            commentCnt
                    );
                }).toList();
    }

    @Transactional
    public ReviewCreateResponse createReview(Long memberId, ReviewCreateRequest request) {
        if (reviewRepository.existsByReservationId(request.reservationId()))
            throw new ReviewException(ALREADY_WRITE);

        Reservation reservation = findReservation(request.reservationId());
        Member member = findMember(memberId);
        Review review = Review.builder()
                .reservation(reservation)
                .content(request.content())
                .member(member)
                .build();
        Review save = reviewRepository.save(review);
        return new ReviewCreateResponse(save.getId());
    }

    private Reservation findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReviewException(INVALID_RESERVATION));
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ReviewException(INVALID_AUTHORITY));
    }
}
