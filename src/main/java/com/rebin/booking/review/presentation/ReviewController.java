package com.rebin.booking.review.presentation;

import com.rebin.booking.auth.domain.Accessor;
import com.rebin.booking.auth.domain.Auth;
import com.rebin.booking.review.dto.request.ReviewCreateRequest;
import com.rebin.booking.review.dto.response.ReviewCreateResponse;
import com.rebin.booking.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    @PostMapping
    public ResponseEntity<ReviewCreateResponse> createReview(@Auth Accessor accessor,
                                                             @RequestBody ReviewCreateRequest request){
        return ResponseEntity.ok(reviewService.createReview(accessor.getMemberId(), request));
    }
}
