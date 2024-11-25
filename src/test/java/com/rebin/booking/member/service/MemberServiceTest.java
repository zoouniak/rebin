package com.rebin.booking.member.service;

import com.rebin.booking.common.excpetion.MemberException;
import com.rebin.booking.member.domain.Member;
import com.rebin.booking.member.domain.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.rebin.booking.common.excpetion.ErrorCode.EXCEED_NAME_LENGTH;
import static com.rebin.booking.common.excpetion.ErrorCode.INVALID_PHONE_FORMAT;
import static com.rebin.booking.member.type.ProviderType.GOOGLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;
    private static Member memberFixture = new Member("1", "email", "nickname", GOOGLE);

    @Test
    @DisplayName("이름을 변경한다.")
    void editName() {
        // given
        when(memberRepository.findById(any()))
                .thenReturn(Optional.ofNullable(memberFixture));
        String expected = "newName";

        // when
        memberService.editName(1L, expected);

        // then
        Assertions.assertThat(memberFixture.getName()).isEqualTo(expected);
    }

    @Test
    @DisplayName("이름 변경 시 이름 길이가 제한을 초과하면 예외가 발생해야 한다.")
    void editName_exceedNameLength(){
        when(memberRepository.findById(any()))
                .thenReturn(Optional.ofNullable(memberFixture));
        String expected = "newName_exceed_name_unavailable";

        assertThatThrownBy(() -> memberService.editName(1L, expected))
                .isInstanceOf(MemberException.class)
                .hasMessage(EXCEED_NAME_LENGTH.getMsg());
    }

    @Test
    @DisplayName("전화번호 변경 시 잘못된 전화번호 형식을 사용하면 예외가 발생한다.")
    void editPhone_invalidNumberFormat() {
        // given
        String expected = "newPhone";

        // when & then
        assertThatThrownBy(() -> memberService.editPhone(1L, expected))
                .isInstanceOf(MemberException.class)
                .hasMessage(INVALID_PHONE_FORMAT.getMsg());
    }

    @Test
    @DisplayName("전화번호를 올바른 형식으로 변경한다.")
    void editPhone() {
        // given
        when(memberRepository.findById(any()))
                .thenReturn(Optional.ofNullable(memberFixture));
        String expected = "010-1111-1111";

        // when
        memberService.editPhone(1L, expected);

        // then
        assertThat(memberFixture.getPhone()).isEqualTo(expected);
    }

    @Test
    @DisplayName("닉네임을 변경한다.")
    void editNickname() {
        // given
        when(memberRepository.findById(any()))
                .thenReturn(Optional.ofNullable(memberFixture));
        String expected = "딸기모찌붕어빠앙";

        // when
        memberService.editNickname(1L, expected);

        // then
        Assertions.assertThat(memberFixture.getNickname()).isEqualTo(expected);
    }

    @Test
    @DisplayName("닉네임 변경 시 이름 길이가 제한을 초과하면 예외가 발생해야 한다.")
    void editNickname_exceedNameLength(){
        when(memberRepository.findById(any()))
                .thenReturn(Optional.ofNullable(memberFixture));
        String expected = "newNickname_exceed_name_unavailable";

        assertThatThrownBy(() -> memberService.editNickname(1L, expected))
                .isInstanceOf(MemberException.class)
                .hasMessage(EXCEED_NAME_LENGTH.getMsg());
    }
}