package com.rebin.booking.notice.presentation;

import com.rebin.booking.auth.domain.Accessor;
import com.rebin.booking.auth.domain.AdminAuth;
import com.rebin.booking.auth.domain.AdminOnly;
import com.rebin.booking.notice.dto.request.NoticeRequest;
import com.rebin.booking.notice.dto.response.NoticeResponse;
import com.rebin.booking.notice.service.AdminNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/notices")
@RequiredArgsConstructor
public class AdminNoticeController {
    private final AdminNoticeService adminNoticeService;

    @Operation(summary = "공지사항 등록")
    @PostMapping
    @AdminOnly
    public ResponseEntity<NoticeResponse> saveNotice(
            @AdminAuth Accessor accessor,
            @RequestBody @Valid NoticeRequest request) {
        return ResponseEntity.ok(adminNoticeService.saveNotice(request));
    }

    @Operation(summary = "공지사항 수정")
    @PutMapping("/{noticeId}")
    @AdminOnly
    public ResponseEntity<Void> updateNotice(
            @AdminAuth Accessor accessor,
            @PathVariable Long noticeId,
            @RequestBody @Valid NoticeRequest request) {
        adminNoticeService.updateNotice(noticeId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "공지사항 삭제")
    @DeleteMapping("/{noticeId}")
    @AdminOnly
    public ResponseEntity<Void> deleteNotice(
            @AdminAuth Accessor accessor,
            @PathVariable Long noticeId) {
        adminNoticeService.deleteNotice(noticeId);
        return ResponseEntity.noContent().build();
    }
}
