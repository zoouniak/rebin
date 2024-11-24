package com.rebin.booking.review.dto.response;

import com.rebin.booking.review.domain.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String writer,
        String content,
        LocalDateTime createdAt
) {
    public static CommentResponse from(Comment comment){
        return new CommentResponse(
                comment.getId(),
                comment.getMember().getNickname(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
