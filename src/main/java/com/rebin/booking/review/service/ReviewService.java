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
                    int helpCnt = getHelpCnt(review);
                    boolean isHelped = isHelped(memberId, review);
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
    public ReviewCreateResponse createReview(final Long memberId, final ReviewCreateRequest request) {
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

    public ReviewResponse getReview(final Long memberId, final Long reviewId) {
        Review review = findReview(reviewId);
        int helpCnt = getHelpCnt(review);
        boolean isHelped = isHelped(memberId, review);
        return ReviewResponse.of(review, helpCnt, isHelped);
    }

    @Transactional
    public void editReview(final Long memberId, final Long reviewId, final String content) {
        validReviewWithMember(memberId, reviewId);
        Review review = findReview(reviewId);

        review.editContent(content);
    }

    @Transactional
    public void deleteReview(final Long memberId, final Long reviewId) {
        validReviewWithMember(memberId, reviewId);
        reviewRepository.deleteById(reviewId);
    }

    private boolean isHelped(final Long memberId, final Review review) {
        return reviewHelpRepository.existsByMemberIdAndReviewId(memberId, review.getId());
    }

    private int getHelpCnt(final Review review) {
        return reviewHelpRepository.countByReviewId(review.getId());
    }

    private Review findReview(final Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewException(INVALID_REVIEW));
    }

    private Reservation findReservation(final Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReviewException(INVALID_RESERVATION));
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ReviewException(INVALID_AUTHORITY));
    }

    private void validReviewWithMember(final Long memberId, final Long reviewId) {
        if (!reviewRepository.existsByIdAndMemberId(reviewId, memberId))
            throw new ReviewException(INVALID_REVIEW);
    }


}
