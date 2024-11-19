package com.rebin.booking.review.service;

import com.rebin.booking.common.excpetion.ReviewException;
import com.rebin.booking.member.domain.repository.MemberRepository;
import com.rebin.booking.reservation.domain.repository.ReservationRepository;
import com.rebin.booking.review.domain.repository.ReviewHelpRepository;
import com.rebin.booking.review.domain.repository.ReviewRepository;
import com.rebin.booking.review.dto.request.ReviewCreateRequest;
import com.rebin.booking.review.dto.request.ReviewEditRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @InjectMocks
    ReviewService reviewService;
    @Mock
    ReviewRepository reviewRepository;
    @Mock
    ReviewHelpRepository reviewHelpRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    ReservationRepository reservationRepository;

    @Test
    void 이미_리뷰한_예약에_리뷰를_작성한다() {
        ReviewCreateRequest request = new ReviewCreateRequest(1L, "리뷰 내용");
        when(reviewRepository.existsByReservationId(any())).thenReturn(true);

        ReviewException reviewException = assertThrows(ReviewException.class, () -> reviewService.createReview(1L, request));

        Assertions.assertEquals("V001", reviewException.getCode());
    }

    @Test
    void 존재하지않는_리뷰를_조회한다() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        ReviewException reviewException = assertThrows(ReviewException.class, () -> reviewService.getReview(1L, 1L));

        Assertions.assertEquals("V002", reviewException.getCode());
    }

    @Test
    void 자신이_작성하지않은_리뷰를_수정한다() {
        when(reviewRepository.existsByIdAndMemberId(1L, 1L)).thenReturn(false);
        ReviewEditRequest request = new ReviewEditRequest("내용입니다길게작성해야하지..");

        ReviewException reviewException = assertThrows(ReviewException.class, () -> reviewService.editReview(1L, 1L, request.content()));

        assertEquals("V002", reviewException.getCode());
    }

    @Test
    void 자신이_작성하지않은_리뷰를_삭제한다(){
        when(reviewRepository.existsByIdAndMemberId(1L, 1L)).thenReturn(false);

        ReviewException reviewException = assertThrows(ReviewException.class, () -> reviewService.deleteReview(1L, 1L));

        assertEquals("V002", reviewException.getCode());
    }


}