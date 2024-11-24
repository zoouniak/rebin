package com.rebin.booking.image.service;

import com.rebin.booking.common.excpetion.ImageException;
import com.rebin.booking.image.domain.ImageFile;
import com.rebin.booking.image.dto.response.ImageResponse;
import com.rebin.booking.image.infrastrcture.ImageUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.rebin.booking.common.excpetion.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ImageService {
    private static final int MAX_IMAGE_LIST_SIZE = 10;
    private static final int EMPTY_IMAGE_LIST_SIZE = 0;
    private final ImageUploader imageUploader;

    public ImageResponse save(List<MultipartFile> images) {
        validateSizeOfImages(images.size());
        List<ImageFile> imageFiles = images.stream()
                .map(ImageFile::new)
                .toList();
        List<String> imageNames = uploadImages(imageFiles);
        return new ImageResponse(imageNames);
    }

    private void validateSizeOfImages(int size) {
        if (size > MAX_IMAGE_LIST_SIZE)
            throw new ImageException(EXCEED_IMAGE_LIST_SIZE);
        if (size == EMPTY_IMAGE_LIST_SIZE)
            throw new ImageException(EMPTY_IMAGE_LIST);
    }

    private List<String> uploadImages(List<ImageFile> imageFiles) {
        List<String> imageNames = imageUploader.uploadImages(imageFiles);
        if (imageFiles.size() != imageNames.size())
            throw new ImageException(INVALID_IMG_PATH);
        return imageNames;
    }
}
