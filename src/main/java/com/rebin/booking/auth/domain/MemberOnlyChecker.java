package com.rebin.booking.auth.domain;


import com.rebin.booking.common.excpetion.AuthException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.rebin.booking.common.excpetion.ErrorCode.INVALID_AUTHORITY;


@Aspect
@Component
public class MemberOnlyChecker {

    @Before("@annotation(com.rebin.booking.auth.domain.MemberOnly)")
    public void check(final JoinPoint joinPoint) {

        Arrays.stream(joinPoint.getArgs())
                .filter(Accessor.class::isInstance)
                .map(Accessor.class::cast)
                .filter(Accessor::isMember)
                .findFirst()
                .orElseThrow(() -> new AuthException(INVALID_AUTHORITY));
    }
}