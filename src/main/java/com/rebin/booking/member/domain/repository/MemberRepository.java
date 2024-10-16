package com.rebin.booking.member.domain.repository;

import com.rebin.booking.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByIdAndEmail(Long memberId, String email);
}
