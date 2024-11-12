package com.rebin.booking.notice.service;

import com.rebin.booking.common.excpetion.NoticeException;
import com.rebin.booking.notice.domain.Notice;
import com.rebin.booking.notice.domain.repository.NoticeRepository;
import com.rebin.booking.notice.dto.request.NoticeRequest;
import com.rebin.booking.notice.dto.response.NoticeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.rebin.booking.common.excpetion.ErrorCode.INVALID_NOTICE;

@Service
@RequiredArgsConstructor
public class AdminNoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeResponse saveNotice(final NoticeRequest request) {
        Notice notice = Notice.builder()
                .title(request.title())
                .content(request.content())
                .build();
        Notice save = noticeRepository.save(notice);
        return NoticeResponse.of(save);
    }

    public NoticeResponse getNotice(final Long noticeId) {
        Notice notice = findNotice(noticeId);
        return NoticeResponse.of(notice);
    }

    public void updateNotice(final Long noticeId, final NoticeRequest request) {
        Notice notice = findNotice(noticeId);
        Notice updateNotice = Notice.builder()
                .id(notice.getId())
                .title(request.title())
                .content(request.content())
                .build();
        noticeRepository.save(updateNotice);
    }

    public void deleteNotice(final Long noticeId) {
        noticeRepository.deleteById(noticeId);
    }

    private Notice findNotice(final Long noticeId) {
        return noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeException(INVALID_NOTICE));
    }


}
