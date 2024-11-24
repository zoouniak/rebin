package com.rebin.booking.image.service;

import com.rebin.booking.common.excpetion.ImageException;
import com.rebin.booking.image.infrastrcture.ImageUploader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {
    private static final int MAX_IMAGE_LIST_SIZE = 10;
    @InjectMocks
    private ImageService imageService;
    @Mock
    private ImageUploader imageUploader;

    @DisplayName("이미지 수가 0개이면 예외가 발생한다.")
    @Test
    void 이미지_업로드_실패1() {
        List<MultipartFile> images = List.of();
        ImageException imageException = Assertions.assertThrows(ImageException.class, () -> imageService.save(images));
        Assertions.assertEquals("I004", imageException.getCode());
    }

    @DisplayName("이미지 수가 최대 개수를 초과하면 예외가 발생한다.")
    @Test
    void 이미지_업로드_실패2() throws IOException {
        final MockMultipartFile file = new MockMultipartFile(
                "images",
                "static/images/logo.png",
                "image/png",
                new FileInputStream("./src/test/resources/static/images/logo.png")
        );
        List<MultipartFile> images = new ArrayList<>(Collections.nCopies(MAX_IMAGE_LIST_SIZE + 1, file));
        ImageException imageException = Assertions.assertThrows(ImageException.class, () -> imageService.save(images));
        Assertions.assertEquals("I003", imageException.getCode());
    }
}