package com.rebin.booking.notice.presentation;

import com.rebin.booking.notice.dto.request.NoticeRequest;
import com.rebin.booking.notice.dto.response.NoticeResponse;
import com.rebin.booking.notice.service.AdminNoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/notices")
@RequiredArgsConstructor
public class AdminNoticeController {
    private final AdminNoticeService adminNoticeService;

    @PostMapping
    public ResponseEntity<NoticeResponse> saveNotice(@RequestBody @Valid NoticeRequest request) {
        return ResponseEntity.ok(adminNoticeService.saveNotice(request));
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponse> getNotice(@PathVariable Long noticeId){
        return ResponseEntity.ok(adminNoticeService.getNotice(noticeId));
    }

    @PutMapping("/{noticeId}")
    public ResponseEntity<Void> updateNotice(@PathVariable Long noticeId,
                                             @RequestBody @Valid NoticeRequest request){
        adminNoticeService.updateNotice(noticeId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long noticeId){
        adminNoticeService.deleteNotice(noticeId);
        return ResponseEntity.noContent().build();
    }
}
