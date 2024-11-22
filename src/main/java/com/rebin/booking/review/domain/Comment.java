package com.rebin.booking.review.domain;

import com.rebin.booking.common.domain.BaseTimeEntity;
import com.rebin.booking.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(cascade = {REMOVE}, fetch = LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(cascade = {REMOVE}, fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    @Builder
    public Comment(Long id, Review review, Member member, String content) {
        this.id = id;
        this.review = review;
        this.member = member;
        this.content = content;
    }
}
