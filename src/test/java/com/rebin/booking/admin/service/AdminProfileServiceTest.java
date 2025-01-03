package com.rebin.booking.admin.service;

import com.rebin.booking.admin.config.PasswordEncoder;
import com.rebin.booking.admin.domain.Admin;
import com.rebin.booking.admin.domain.repository.AdminRepository;
import com.rebin.booking.admin.dto.request.AdminPasswordRequest;
import com.rebin.booking.common.excpetion.AdminException;
import com.rebin.booking.common.excpetion.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminProfileServiceTest {
    @InjectMocks
    private AdminProfileService adminProfileService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void changeId() {
        Admin admin = new Admin(1L, "loginId", "password");
        String newId = "newId";
        when(adminRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(admin));

        // when
        adminProfileService.changeId(1L, newId);

        // then
        Assertions.assertThat(admin.getLoginId()).isEqualTo(newId);
    }

    @Test
    void changePassword() {
        // given
        AdminPasswordRequest request = new AdminPasswordRequest("original", "newPassword");
        Admin admin = new Admin(1L, "loginId", "password");
        when(adminRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(true);

        // when
        adminProfileService.changePassword(1L, request);

        // then
        Assertions.assertThat(admin.getPassword()).isEqualTo(request.newPassword());
    }

    @Test
    void changePassword_unMatchPassword() {
        // given
        AdminPasswordRequest request = new AdminPasswordRequest("original", "newPassword");
        Admin admin = new Admin(1L, "loginId", "password");
        when(adminRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(false);

        // when & then
        Assertions.assertThatThrownBy(() -> adminProfileService.changePassword(1L, request))
                .isInstanceOf(AdminException.class)
                .hasMessage(ErrorCode.INVALID_ADMIN.getMsg());
    }
}