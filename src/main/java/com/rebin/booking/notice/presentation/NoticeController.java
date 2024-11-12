package com.rebin.booking.notice.presentation;

import com.rebin.booking.notice.dto.response.NoticePageResponse;
import com.rebin.booking.notice.dto.response.NoticeResponse;
import com.rebin.booking.notice.service.NoticeReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notices")
public class NoticeController {
    private final NoticeReadService noticeReadService;

    @GetMapping
    public ResponseEntity<NoticePageResponse> getNotices(@RequestParam int page) {
        return ResponseEntity.ok(noticeReadService.getNotices(page));
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponse> getNotice(@PathVariable Long noticeId){
        return ResponseEntity.ok(noticeReadService.getNotice(noticeId));
    }
}
