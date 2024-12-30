package com.rebin.booking.info.presentation;

import com.rebin.booking.info.dto.InfoDto;
import com.rebin.booking.info.service.AdminInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/infos")
@RequiredArgsConstructor
public class AdminInfoController {
    private final AdminInfoService adminInfoService;

    @GetMapping
    public ResponseEntity<List<InfoDto>> getInfo() {
        return ResponseEntity.ok(adminInfoService.getInfoList());
    }

    @PostMapping
    public ResponseEntity<Void> saveInfo(@RequestBody List<InfoDto> infoDtoList) {
        adminInfoService.saveInfoList(infoDtoList);
        return ResponseEntity.ok().build();
    }
}
