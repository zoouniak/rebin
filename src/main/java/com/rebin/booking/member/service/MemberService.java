package com.rebin.booking.member.service;

import com.rebin.booking.common.excpetion.MemberException;
import com.rebin.booking.member.domain.Member;
import com.rebin.booking.member.domain.repository.MemberRepository;
import com.rebin.booking.member.dto.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.rebin.booking.common.excpetion.ErrorCode.INVALID_MEMBER;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponse getProfile(Long memberId) {
        Member member = findMember(memberId);

        return MemberResponse.from(member);
    }

    @Transactional
    public void editName(Long memberId, String name) {
        Member member = findMember(memberId);
        member.updateName(name);
    }

    @Transactional
    public void editPhone(Long memberId, String phone) {
        Member member = findMember(memberId);
        member.updatePhone(phone);
    }

    @Transactional
    public void editNickname(Long memberId, String nickname) {
        Member member = findMember(memberId);
        member.updateNickname(nickname);
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(INVALID_MEMBER));
    }
}
