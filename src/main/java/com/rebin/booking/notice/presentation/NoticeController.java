package com.rebin.booking.notice.presentation;

import com.rebin.booking.notice.dto.response.NoticePageResponse;
import com.rebin.booking.notice.dto.response.NoticeResponse;
import com.rebin.booking.notice.service.NoticeReadService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notices")
public class NoticeController {
    private final NoticeReadService noticeReadService;

    @Operation(summary = "공지사항 목록 조회")
    @GetMapping
    public ResponseEntity<NoticePageResponse> getNotices(@RequestParam int page) {
        return ResponseEntity.ok(noticeReadService.getNotices(page));
    }

    @Operation(summary = "공지사항 상세 조회")
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponse> getNotice(@PathVariable Long noticeId){
        return ResponseEntity.ok(noticeReadService.getNotice(noticeId));
    }
}
