package com.rebin.booking.notice.service;

import com.rebin.booking.common.excpetion.ErrorCode;
import com.rebin.booking.common.excpetion.NoticeException;
import com.rebin.booking.notice.domain.Notice;
import com.rebin.booking.notice.domain.repository.NoticeRepository;
import com.rebin.booking.notice.dto.request.NoticeRequest;
import com.rebin.booking.notice.dto.response.NoticeResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminNoticeServiceTest {
    @InjectMocks
    private AdminNoticeService adminNoticeService;
    @Mock
    private NoticeRepository noticeRepository;

    @Test
    @DisplayName("공지사항을 저장한다.")
    void saveNotice() {
        // given
        NoticeRequest request = new NoticeRequest("title", "content");
        Notice expected = Notice.builder()
                .id(1L)
                .title("title")
                .content("content")
                .build();

        when(noticeRepository.save(Mockito.any()))
                .thenReturn(expected);

        // when
        NoticeResponse actual = adminNoticeService.saveNotice(request);

        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(NoticeResponse.from(expected));
    }

    @Test
    @DisplayName("존재하지 않는 공지사항을 수정하면 에러가 발생한다.")
    void updateNotice_InvalidNotice(){
        when(noticeRepository.findById(any()))
                .thenReturn(Optional.empty());
        NoticeRequest request = new NoticeRequest("title","content");

        // when & then
        Assertions.assertThatThrownBy( ()->adminNoticeService.updateNotice(1L, request))
                .isInstanceOf(NoticeException.class)
                .hasMessage(ErrorCode.INVALID_NOTICE.getMsg());
    }

    @Test
    @DisplayName("공지사항을 수정한다.")
    void updateNotice(){
        // when
        Notice notice  = new Notice(1L,"title","content");
        NoticeRequest request = new NoticeRequest("newTitle","newContent");
        when(noticeRepository.findById(any()))
                .thenReturn(Optional.of(notice));

        // when
        adminNoticeService.updateNotice(1L, request);

        // then
        verify(noticeRepository,times(1)).save(any());
    }

    @Test
    @DisplayName("공지사항을 삭제한다.")
    void deleteNotice(){
        // when
        adminNoticeService.deleteNotice(1L);

        // then
        verify(noticeRepository,times(1)).deleteById(1L);
    }

}