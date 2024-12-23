package com.rebin.booking.review.service;

import com.rebin.booking.common.excpetion.CommentException;
import com.rebin.booking.common.excpetion.MemberException;
import com.rebin.booking.common.excpetion.ReviewException;
import com.rebin.booking.member.domain.Member;
import com.rebin.booking.member.domain.repository.MemberRepository;
import com.rebin.booking.review.domain.Review;
import com.rebin.booking.review.domain.repository.CommentRepository;
import com.rebin.booking.review.domain.repository.ReviewRepository;
import com.rebin.booking.review.dto.request.CommentRequest;
import com.rebin.booking.review.dto.response.CommentResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.rebin.booking.common.excpetion.ErrorCode.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("댓글을 작성한다.")
    void createComment() {
        // given
        Member member = FixtureUtil.member();
        Review review = FixtureUtil.review();

        when(memberRepository.findById(any()))
                .thenReturn(Optional.of(member));
        when(reviewRepository.findById(any()))
                .thenReturn(Optional.of(review));
        CommentRequest request = new CommentRequest("댓글을 달아봅니다.");
        when(commentRepository.save(any()))
                .thenReturn(FixtureUtil.comment(request.content()));

        // when
        CommentResponse actual = commentService.createComment(1L, 1L, request);

        // then
        Assertions.assertThat(actual.content()).isEqualTo(request.content());
        verify(commentRepository).save(any());
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 댓글을 작성하려고 하면 에러가 발생한다.")
    void createComment_invalidMember(){
        when(memberRepository.findById(any()))
                .thenReturn(Optional.empty());
        CommentRequest request = new CommentRequest("댓글을 달아봅니다.");

        // when & then
        Assertions.assertThatThrownBy(()-> commentService.createComment(1L,1L,request))
                .isInstanceOf(MemberException.class)
                .hasMessage(INVALID_MEMBER.getMsg());
    }

    @Test
    @DisplayName("존재하지 않는 리뷰에 댓글을 작성하려고 하면 에러가 발생한다.")
    void createComment_invalidReview(){
        when(memberRepository.findById(any()))
                .thenReturn(Optional.of(FixtureUtil.member()));
        when(reviewRepository.findById(any()))
                .thenReturn(Optional.empty());
        CommentRequest request = new CommentRequest("댓글을 달아봅니다.");

        // when & then
        Assertions.assertThatThrownBy(() -> commentService.createComment(1L,1L,request))
                .isInstanceOf(ReviewException.class)
                .hasMessage(INVALID_REVIEW.getMsg());
    }

    @Test
    @DisplayName("댓글을 수정한다.")
    void editComment(){
        when(commentRepository.existsByIdAndMemberId(any(),any()))
                .thenReturn(true);
        when(commentRepository.findById(any()))
                .thenReturn(Optional.of(FixtureUtil.comment("원래 댓글")));
        CommentRequest request = new CommentRequest("댓글을 달아봅니다.");
        // when
        CommentResponse actual = commentService.editComment(1L, 1L, request);

        // then
        Assertions.assertThat(actual.content()).isEqualTo(request.content());
    }

    @Test
    @DisplayName("댓글 작성자가 아닌 사용자가 댓글을 수정하려고 하면 에러가 발생한다.")
    void editComment_NotAuthor(){
        when(commentRepository.existsByIdAndMemberId(any(),any()))
                .thenReturn(false);
        CommentRequest request = new CommentRequest("댓글을 달아봅니다.");

        // when & then
        Assertions.assertThatThrownBy(() -> commentService.editComment(1L,1L,request))
                .isInstanceOf(CommentException.class)
                .hasMessage(NOT_COMMENT_AUTHOR.getMsg());
    }

    @Test
    @DisplayName("댓글을 삭제한다.")
    void deleteComment(){
        when(commentRepository.existsByIdAndMemberId(any(),any()))
                .thenReturn(true);

        // when
        commentService.deleteComment(1L,1L);

        // then
        verify(commentRepository).deleteById(1L);
    }

    @Test
    @DisplayName("댓글 작성자가 아닌 사용자가 댓글을 삭제하려고 하면 에러가 발생한다.")
    void deleteComment_notAuthor(){
        when(commentRepository.existsByIdAndMemberId(any(),any()))
                .thenReturn(false);

        // when & then
        Assertions.assertThatThrownBy(()-> commentService.deleteComment(1L,1L))
                .isInstanceOf(CommentException.class)
                .hasMessage(NOT_COMMENT_AUTHOR.getMsg());
    }


}