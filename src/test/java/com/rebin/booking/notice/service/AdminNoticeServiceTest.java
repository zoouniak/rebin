package com.rebin.booking.notice.service;

import com.rebin.booking.notice.domain.Notice;
import com.rebin.booking.notice.domain.repository.NoticeRepository;
import com.rebin.booking.notice.dto.request.NoticeRequest;
import com.rebin.booking.notice.dto.response.NoticeResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminNoticeServiceTest {
    @InjectMocks
    private AdminNoticeService adminNoticeService;
    @Mock
    private NoticeRepository noticeRepository;

    @Test
    void saveNotice() {
        // given
        NoticeRequest request = new NoticeRequest("title", "content");
        Notice expected = Notice.builder()
                .id(1L)
                .title("title")
                .content("content")
                .build();

        Mockito.when(noticeRepository.save(Mockito.any()))
                .thenReturn(expected);

        // when
        NoticeResponse actual = adminNoticeService.saveNotice(request);

        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(NoticeResponse.from(expected));
    }

}