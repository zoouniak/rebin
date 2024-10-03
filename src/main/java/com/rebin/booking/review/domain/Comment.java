package com.rebin.booking.review.domain;

import com.rebin.booking.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(cascade = {REMOVE})
    @JoinColumn(name = "review_id")
    private Review review;

    private String content;

}
