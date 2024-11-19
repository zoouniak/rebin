package com.rebin.booking.member.presentation;

import com.rebin.booking.auth.domain.Accessor;
import com.rebin.booking.auth.domain.Auth;
import com.rebin.booking.auth.domain.MemberOnly;
import com.rebin.booking.member.dto.request.NameRequest;
import com.rebin.booking.member.dto.request.NicknameRequest;
import com.rebin.booking.member.dto.request.PhoneRequest;
import com.rebin.booking.member.dto.response.MemberResponse;
import com.rebin.booking.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    @MemberOnly
    public MemberResponse getProfile(@Auth Accessor accessor) {
        return memberService.getProfile(accessor.getMemberId());
    }

    @PatchMapping("/name")
    @MemberOnly
    public void editName(@Auth Accessor accessor,
                         @RequestBody NameRequest request) {
        memberService.editName(accessor.getMemberId(), request.name());
    }

    @PatchMapping("/phone")
    @MemberOnly
    public void editPhone(@Auth Accessor accessor,
                         @RequestBody PhoneRequest request) {
        memberService.editPhone(accessor.getMemberId(), request.phone());
    }

    @PatchMapping("/nickname")
    @MemberOnly
    public void editNickname(@Auth Accessor accessor,
                         @RequestBody NicknameRequest request) {
        memberService.editNickname(accessor.getMemberId(), request.nickname());
    }
}
