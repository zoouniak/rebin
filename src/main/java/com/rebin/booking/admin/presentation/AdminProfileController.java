package com.rebin.booking.admin.presentation;

import com.rebin.booking.admin.dto.request.AdminIdRequest;
import com.rebin.booking.admin.dto.request.AdminPasswordRequest;
import com.rebin.booking.admin.service.AdminProfileService;
import com.rebin.booking.auth.domain.Accessor;
import com.rebin.booking.auth.domain.AdminAuth;
import com.rebin.booking.auth.domain.AdminOnly;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/profile")
public class AdminProfileController {
    private final AdminProfileService adminProfileService;

    @PostMapping("/id")
    @AdminOnly
    public ResponseEntity<Void> changeLoginId(@AdminAuth Accessor accessor,
                                              @RequestBody AdminIdRequest request){
        adminProfileService.changeId(accessor.getMemberId(), request.newId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password")
    @AdminOnly
    public ResponseEntity<Void> changePassword(@AdminAuth Accessor accessor,
                                               @RequestBody AdminPasswordRequest request){
        adminProfileService.changePassword(accessor.getMemberId(), request);
        return ResponseEntity.noContent().build();
    }
}
