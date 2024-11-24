package com.rebin.booking.review.service;

import com.rebin.booking.common.excpetion.MemberException;
import com.rebin.booking.common.excpetion.ReviewException;
import com.rebin.booking.member.domain.Member;
import com.rebin.booking.member.domain.repository.MemberRepository;
import com.rebin.booking.review.domain.Comment;
import com.rebin.booking.review.domain.Review;
import com.rebin.booking.review.domain.repository.CommentRepository;
import com.rebin.booking.review.domain.repository.ReviewRepository;
import com.rebin.booking.review.dto.request.CommentRequest;
import com.rebin.booking.review.dto.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.rebin.booking.common.excpetion.ErrorCode.INVALID_MEMBER;
import static com.rebin.booking.common.excpetion.ErrorCode.INVALID_REVIEW;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    public CommentResponse createComment(final Long memberId, final Long reviewId, final CommentRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(INVALID_MEMBER));
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewException(INVALID_REVIEW));

        Comment save = commentRepository.save(Comment.builder()
                .member(member)
                .review(review)
                .content(request.content())
                .build());

        return CommentResponse.from(save);
    }
}