package com.rebin.booking.member.domain;

import com.rebin.booking.common.domain.BaseTimeEntity;
import com.rebin.booking.common.excpetion.MemberException;
import com.rebin.booking.member.type.ProviderType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static com.rebin.booking.common.excpetion.ErrorCode.EXCEED_NAME_LENGTH;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, updatable = false)
    private String loginId;

    @Column
    private String name;

    @Column(unique = true)
    private String nickname;

    @Column
    private String phone;

    @Column(updatable = false)
    private String email;

    @Column
    @Enumerated(value = EnumType.STRING)
    private ProviderType provider;

    public Member(String loginId, String email, String nickname, ProviderType provider) {
        this.email = Objects.requireNonNull(email);
        this.loginId = Objects.requireNonNull(loginId);
        this.nickname = Objects.requireNonNull(nickname);
        this.provider = Objects.requireNonNull(provider);
        this.name = "";
        this.phone = "";
    }

    private static final int MAX_NAME_LENGTH = 10;

    public void updateName(String newName) {
        if (newName.length() > MAX_NAME_LENGTH)
            throw new MemberException(EXCEED_NAME_LENGTH);
        this.name = newName;
    }

    public void updatePhone(String newPhone) {
        this.phone = newPhone;
    }

    public void updateNickname(String newNickname) {
        if (newNickname.length() > MAX_NAME_LENGTH)
            throw new MemberException(EXCEED_NAME_LENGTH);
        this.nickname = newNickname;
    }

    public void updateMemberIfNameOrPhoneMissing(String name, String phone) {
        if (this.name.isEmpty())
            this.name = name;
        if (this.phone.isEmpty())
            this.phone = phone;
    }
}
