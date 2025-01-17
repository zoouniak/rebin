package com.rebin.booking.review.domain;

import com.rebin.booking.common.domain.BaseTimeEntity;
import com.rebin.booking.member.domain.Member;
import com.rebin.booking.product.domain.Product;
import com.rebin.booking.reservation.domain.Reservation;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@SQLDelete(sql = "update review set deleted = 1 where id = ?")
@SQLRestriction("deleted = 0")
@NoArgsConstructor(access = PROTECTED)
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column
    private LocalDate shootDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(length = 5000)
    private String content;

    @OneToMany(mappedBy = "review")
    private List<Comment> comments;

    @Column
    @ColumnDefault(value = "false")
    private boolean deleted = false;

    @Builder
    public Review(Reservation reservation, Member member, String content,List<Comment> comments) {
        this.reservation = reservation;
        this.shootDate = reservation.getShootDate();
        this.member = member;
        this.product = reservation.getProduct();
        this.content = content;
        this.comments = comments;
    }

    public void editContent(String content) {
        this.content = content;
    }
}
