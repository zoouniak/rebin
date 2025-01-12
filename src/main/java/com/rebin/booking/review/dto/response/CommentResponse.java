package com.rebin.booking.review.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rebin.booking.review.domain.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String writer,
        String content,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
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
