package com.rebin.booking.notice.service;

import com.rebin.booking.common.excpetion.NoticeException;
import com.rebin.booking.notice.domain.Notice;
import com.rebin.booking.notice.domain.repository.NoticeRepository;
import com.rebin.booking.notice.dto.response.NoticePageResponse;
import com.rebin.booking.notice.dto.response.NoticeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rebin.booking.common.excpetion.ErrorCode.INVALID_NOTICE;

@Service
@RequiredArgsConstructor
public class NoticeReadService {
    private final NoticeRepository noticeRepository;

    public NoticePageResponse getNotices(int page){
        Page<Notice> noticePage = noticeRepository.findAll(PageRequest.of(page, 10, Sort.by("id").descending()));
        List<NoticeResponse> notices = noticePage.getContent().stream()
                .map(NoticeResponse::from)
                .toList();
        return new NoticePageResponse(noticePage.getTotalPages(), notices);
    }

    public NoticeResponse getNotice(final Long noticeId) {
        Notice notice = findNotice(noticeId);
        return NoticeResponse.from(notice);
    }

    private Notice findNotice(final Long noticeId) {
        return noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeException(INVALID_NOTICE));
    }
}
