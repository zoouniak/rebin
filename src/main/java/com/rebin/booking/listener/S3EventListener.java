package com.rebin.booking.listener;

import com.amazonaws.services.s3.AmazonS3;
import com.rebin.booking.image.domain.S3ImageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Component
@RequiredArgsConstructor
public class S3EventListener {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.s3.folder}")
    private String folder;
    private final AmazonS3 s3Client;

    @Async
    @Transactional(propagation = REQUIRES_NEW)
    @TransactionalEventListener
    public void deleteImageInS3(S3ImageEvent event) {
        final String imageName = event.url();
        s3Client.deleteObject(bucket,folder+imageName);
    }
}
