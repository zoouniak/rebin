package com.rebin.booking.image.presentation;

import com.rebin.booking.image.ImageService;
import com.rebin.booking.image.dto.response.ImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<ImageResponse> uploadImages(@RequestPart List<MultipartFile> images) {
        return ResponseEntity.ok(imageService.save(images));
    }
}