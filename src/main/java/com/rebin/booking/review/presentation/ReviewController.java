package com.rebin.booking.review.presentation;

import com.rebin.booking.auth.domain.Accessor;
import com.rebin.booking.auth.domain.Auth;
import com.rebin.booking.auth.domain.MemberOnly;
import com.rebin.booking.review.dto.request.ReviewCreateRequest;
import com.rebin.booking.review.dto.request.ReviewEditRequest;
import com.rebin.booking.review.dto.response.ReviewCreateResponse;
import com.rebin.booking.review.dto.response.ReviewDetailResponse;
import com.rebin.booking.review.dto.response.ReviewResponse;
import com.rebin.booking.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @MemberOnly
    public ResponseEntity<ReviewCreateResponse> createReview(@Auth Accessor accessor,
                                                             @RequestBody @Valid ReviewCreateRequest request) {
        return ResponseEntity.ok(reviewService.createReview(accessor.getMemberId(), request));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getReview(@Auth Accessor accessor,
                                                    @PathVariable(value = "reviewId") Long reviewId) {
        return ResponseEntity.ok(reviewService.getReview(accessor.getMemberId(), reviewId));
    }

    @PatchMapping("/{reviewId}")
    @MemberOnly
    public ResponseEntity<Void> editReview(@Auth Accessor accessor,
                                           @PathVariable(value = "reviewId") Long reviewId,
                                           @RequestBody @Valid ReviewEditRequest request) {
        reviewService.editReview(accessor.getMemberId(), reviewId, request.content());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{reviewId}")
    @MemberOnly
    public ResponseEntity<Void> deleteReview(@Auth Accessor accessor,
                                             @PathVariable(value = "reviewId") Long reviewId){
        reviewService.deleteReview(accessor.getMemberId(), reviewId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    @MemberOnly
    public ResponseEntity<List<ReviewDetailResponse>> getReviewByMember(@Auth Accessor accessor){
        return ResponseEntity.ok(reviewService.getReviewByMember(accessor.getMemberId()));
    }

}
