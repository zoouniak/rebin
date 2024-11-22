package com.rebin.booking.review.presentation;

import com.rebin.booking.auth.domain.Accessor;
import com.rebin.booking.auth.domain.Auth;
import com.rebin.booking.review.dto.request.CommentRequest;
import com.rebin.booking.review.dto.response.CommentResponse;
import com.rebin.booking.review.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/reviews/{reviewId}")
    public ResponseEntity<CommentResponse> createComment(
            @Auth Accessor accessor,
            @PathVariable Long reviewId,
            @RequestBody @Valid CommentRequest request
    ) {
        return ResponseEntity.ok(commentService.createComment(accessor.getMemberId(), reviewId, request));
    }
}
