package com.rebin.booking.reservation.dto.response;

import com.rebin.booking.member.domain.Member;

public record MemberInfoResponse(
        String name,
        String email,
        String phone
) {
    public static MemberInfoResponse from(Member member){
        return  new MemberInfoResponse(
                member.getName(),
                member.getEmail(),
                member.getPhone()
        );
    }
}
