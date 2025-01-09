package com.rebin.booking.admin.service;

import com.rebin.booking.admin.config.PasswordEncoder;
import com.rebin.booking.admin.domain.Admin;
import com.rebin.booking.admin.domain.repository.AdminRepository;
import com.rebin.booking.admin.dto.request.AdminPasswordRequest;
import com.rebin.booking.common.excpetion.AdminException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.rebin.booking.common.excpetion.ErrorCode.INCORRECT_PASSWORD;
import static com.rebin.booking.common.excpetion.ErrorCode.INVALID_ADMIN;

@Service
@RequiredArgsConstructor
public class AdminProfileService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void changeId(Long adminId, String newLoginId){
        Admin admin = getAdmin(adminId);

        admin.changeLoginId(newLoginId);
    }

    @Transactional
    public void changePassword(Long adminId, AdminPasswordRequest request){
        Admin admin = getAdmin(adminId);

       if(!passwordEncoder.matches(request.originalPassword(), admin.getPassword()))
            throw new AdminException(INCORRECT_PASSWORD);

        String encodePassword = passwordEncoder.encode(request.newPassword());
        admin.changePassword(encodePassword);
    }

    private Admin getAdmin(Long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminException(INVALID_ADMIN));
    }
}
