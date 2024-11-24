package com.rebin.booking.member.presentation;

import com.rebin.booking.auth.domain.Accessor;
import com.rebin.booking.auth.domain.Auth;
import com.rebin.booking.auth.domain.MemberOnly;
import com.rebin.booking.member.dto.request.NameRequest;
import com.rebin.booking.member.dto.request.NicknameRequest;
import com.rebin.booking.member.dto.request.PhoneRequest;
import com.rebin.booking.member.dto.response.MemberResponse;
import com.rebin.booking.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "프로필 조회")
    @GetMapping
    @MemberOnly
    public MemberResponse getProfile(@Auth Accessor accessor) {
        return memberService.getProfile(accessor.getMemberId());
    }

    @Operation(summary = "이름 변경")
    @PatchMapping("/name")
    @MemberOnly
    public ResponseEntity<Void> editName(@Auth Accessor accessor,
                                         @RequestBody NameRequest request) {
        memberService.editName(accessor.getMemberId(), request.name());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "전화번호 변경")
    @PatchMapping("/phone")
    @MemberOnly
    public ResponseEntity<Void> editPhone(@Auth Accessor accessor,
                         @RequestBody PhoneRequest request) {
        memberService.editPhone(accessor.getMemberId(), request.phone());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "닉네임 변경")
    @PatchMapping("/nickname")
    @MemberOnly
    public ResponseEntity<Void> editNickname(@Auth Accessor accessor,
                         @RequestBody NicknameRequest request) {
        memberService.editNickname(accessor.getMemberId(), request.nickname());
        return ResponseEntity.ok().build();
    }
}
