package com.rebin.booking.image.infrastrcture;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.rebin.booking.common.excpetion.ImageException;
import com.rebin.booking.image.domain.ImageFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static com.rebin.booking.common.excpetion.ErrorCode.INVALID_IMAGE;
import static com.rebin.booking.common.excpetion.ErrorCode.INVALID_IMG_PATH;

@Component
@RequiredArgsConstructor
public class ImageUploader {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.s3.folder}")
    private String folder;
    private final AmazonS3 s3Client;

    public List<String> uploadImages(List<ImageFile> images) {
        List<CompletableFuture<String>> imageUploadsFutures = images.stream()
                .map(image -> CompletableFuture.supplyAsync(() -> uploadImage(image)))
                .toList();
        return getUploadedImageNamesFromFutures(imageUploadsFutures);
    }

    private List<String> getUploadedImageNamesFromFutures(List<CompletableFuture<String>> futures) {
        List<String> imageNames = new ArrayList<>();
        futures.forEach(future -> {
            try {
                imageNames.add(future.join());
            } catch (final CompletionException ignored) {
            }
        });
        return imageNames;
    }

    private String uploadImage(ImageFile image) {
        final String path = folder + image.getUuidName();
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());

        try (final InputStream inputStream = image.getInputStream()) {
            s3Client.putObject(bucket, path, inputStream, metadata);
        } catch (AmazonServiceException e) {
            throw new ImageException(INVALID_IMG_PATH);
        } catch (IOException e) {
            throw new ImageException(INVALID_IMAGE);
        }
        return image.getUuidName();
    }
}
