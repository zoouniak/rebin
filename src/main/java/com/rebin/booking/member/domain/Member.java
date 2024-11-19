package com.rebin.booking.member.domain;

import com.rebin.booking.common.domain.BaseTimeEntity;
import com.rebin.booking.member.type.ProviderType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)// todo 공부
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, updatable = false)
    private String loginId;

    @Column
    private String name;

    @Column
    private String nickname;

    @Column(length = 11)
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

    public void updateName(String newName) {
        this.name = newName;
    }

    public void updatePhone(String newPhone) {
        this.phone = newPhone;
    }

    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void updateMemberIfNameOrPhoneMissing(String name, String phone) {
        if (this.name.isEmpty())
            this.name = name;
        if (this.phone.isEmpty())
            this.phone = phone;
    }
}
