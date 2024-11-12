package com.rebin.booking.member.dto.response;


import com.rebin.booking.member.domain.Member;

public record MemberResponse(
        String name,
        String phone,
        String email,
        String nickname
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getName(),
                member.getPhone(),
                member.getEmail(),
                member.getNickname()
        );
    }
}
