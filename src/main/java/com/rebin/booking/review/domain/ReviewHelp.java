package com.rebin.booking.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ReviewHelp {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long reviewId;

    public ReviewHelp(Long id, Long memberId, Long reviewId) {
        this.id = id;
        this.memberId = memberId;
        this.reviewId = reviewId;
    }

    public ReviewHelp(Long memberId, Long reviewId) {
        this.memberId = memberId;
        this.reviewId = reviewId;
    }
}
