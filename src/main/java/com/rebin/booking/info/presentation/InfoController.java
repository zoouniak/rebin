package com.rebin.booking.info.presentation;

import com.rebin.booking.info.dto.InfoDto;
import com.rebin.booking.info.service.InfoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/infos")
public class InfoController {
    private final InfoService infoService;
    @Operation(summary = "소개 조회")
    @GetMapping
    public ResponseEntity<List<InfoDto>> getInfo() {
        return ResponseEntity.ok(infoService.getInfoList());
    }
}
