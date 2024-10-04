package com.rebin.booking.member.domain;

import com.rebin.booking.common.domain.BaseTimeEntity;
import com.rebin.booking.member.type.ProviderType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    public Member(String loginId, String email, ProviderType provider) {
        this.email = email;
        this.loginId = loginId;
        this.provider = provider;
    }
}
