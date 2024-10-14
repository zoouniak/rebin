package com.rebin.booking.review.service;

import com.rebin.booking.review.domain.repository.ReviewHelpRepository;
import com.rebin.booking.review.domain.repository.ReviewRepository;
import com.rebin.booking.review.dto.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewHelpRepository reviewHelpRepository;

    public List<ReviewResponse>

    getReviewsByProduct(final Long memberId, final Long productId, final Pageable pageable) {
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
}
