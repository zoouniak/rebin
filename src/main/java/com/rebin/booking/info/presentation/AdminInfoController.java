package com.rebin.booking.info.presentation;

import com.rebin.booking.info.dto.InfoRequest;
import com.rebin.booking.info.service.AdminInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/infos")
@RequiredArgsConstructor
public class AdminInfoController {
    private final AdminInfoService adminInfoService;

    @PostMapping
    public ResponseEntity<Void> saveInfo(@RequestBody List<InfoRequest> infoRequestList){
        adminInfoService.saveInfoList(infoRequestList);
        return ResponseEntity.ok().build();
    }
}
