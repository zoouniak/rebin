package com.rebin.booking.info.presentation;

import com.rebin.booking.info.dto.InfoDto;
import com.rebin.booking.info.service.AdminInfoService;
import com.rebin.booking.info.service.InfoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/infos")
@RequiredArgsConstructor
public class AdminInfoController {
    private final AdminInfoService adminInfoService;
    private final InfoService infoService;

    @Operation(summary = "관리자 소개 조회")
    @GetMapping
    public ResponseEntity<List<InfoDto>> getInfo() {
        return ResponseEntity.ok(infoService.getInfoList());
    }

    @Operation(summary = "관리자 소개 저장")
    @PostMapping
    public ResponseEntity<Void> saveInfo(@RequestBody List<InfoDto> infoDtoList) {
        adminInfoService.saveInfoList(infoDtoList);
        return ResponseEntity.ok().build();
    }
}
