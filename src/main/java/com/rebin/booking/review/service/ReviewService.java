package com.rebin.booking.review.service;

import com.rebin.booking.common.excpetion.ReviewException;
import com.rebin.booking.member.domain.Member;
import com.rebin.booking.member.domain.repository.MemberRepository;
import com.rebin.booking.product.dto.response.ProductResponse;
import com.rebin.booking.reservation.domain.Reservation;
import com.rebin.booking.reservation.domain.repository.ReservationRepository;
import com.rebin.booking.review.domain.Comment;
import com.rebin.booking.review.domain.Review;
import com.rebin.booking.review.domain.ReviewHelp;
import com.rebin.booking.review.domain.repository.CommentRepository;
import com.rebin.booking.review.domain.repository.ReviewHelpRepository;
import com.rebin.booking.review.domain.repository.ReviewRepository;
import com.rebin.booking.review.dto.request.ReviewCreateRequest;
import com.rebin.booking.review.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final CommentRepository commentRepository;

    public ReviewPageResponse getReviewsByProduct(final Long memberId, final Long productId, final Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByProductIdAndPageable(productId, pageable);


        return new ReviewPageResponse(reviews.getContent().stream()
                .map(review -> {
                    int helpCnt = getHelpCnt(review);
                    boolean isHelped = isHelped(memberId, review);

                    return ReviewResponse.of(
                            review,
                            helpCnt,
                            isHelped
                    );
                }).toList(), reviews.getTotalElements(), reviews.isLast());
    }

    @Transactional
    public ReviewResponse createReview(final Long memberId, final ReviewCreateRequest request) {
        if (reviewRepository.existsByReservationId(request.reservationId()))
            throw new ReviewException(ALREADY_WRITE);

        Reservation reservation = findReservation(request.reservationId());
        Member member = findMember(memberId);

        Review save = reviewRepository.save(Review.builder()
                .reservation(reservation)
                .content(request.content())
                .member(member)
                .build());
        return ReviewResponse.of(save, 0, false);
    }

    public ReviewResponse getReview(final Long memberId, final Long reviewId) {
        Review review = findReview(reviewId);
        int helpCnt = getHelpCnt(review);
        boolean isHelped = isHelped(memberId, review);
        return ReviewResponse.of(review, helpCnt, isHelped);
    }

    public ReviewDetailResponse getReviewDetail(Long memberId, Long reviewId) {
        Review review = findReview(reviewId);
        int helpCnt = getHelpCnt(review);
        boolean isHelped = isHelped(memberId, review);
        final ReviewResponse reviewResponse = ReviewResponse.of(review, helpCnt, isHelped);
        final List<Comment> comments = review.getComments();
        final List<CommentResponse> commentResponses = comments.stream()
                .map(CommentResponse::from)
                .toList();

        return new ReviewDetailResponse(reviewResponse, commentResponses);
    }

    public List<ReviewWithProductResponse> getReviewsByMember(final Long memberId) {
        List<Review> reviews = reviewRepository.findAllByMemberId(memberId);
        return reviews.stream().map(review -> {
            int helpCnt = getHelpCnt(review);
            boolean isHelped = isHelped(memberId, review);
            return new ReviewWithProductResponse(
                    ReviewResponse.of(review, helpCnt, isHelped),
                    ProductResponse.of(review.getProduct())
            );
        }).toList();
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

        // 댓글 먼저 삭제
        commentRepository.deleteByReviewId(reviewId);
        reviewRepository.deleteById(reviewId);
    }

    @Transactional
    public void makeReviewHelp(final Long memberId, final Long reviewId) {
        reviewHelpRepository.save(new ReviewHelp(
                memberId, reviewId
        ));
    }

    @Transactional
    public void removeReviewHelp(final Long memberId, final Long reviewId) {
        reviewHelpRepository.deleteByMemberIdAndReviewId(memberId, reviewId);
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
