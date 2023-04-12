package com.likedancesport;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.likedancesport.common.model.impl.Video;
import com.likedancesport.common.service.storage.S3StorageService;
import com.likedancesport.dao.IVideoDao;
import com.likedancesport.service.IVideoEncodingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
@Slf4j
public class VideoUploadHandlerFunction implements Function<S3Event, Void> {
    private final IVideoEncodingService videoEncodingService;
    private final IVideoDao videoDao;
    private final S3StorageService s3StorageService;

    @Autowired
    public VideoUploadHandlerFunction(IVideoEncodingService videoEncodingService, IVideoDao videoDao, S3StorageService s3StorageService) {
        log.info("---- FUNCTION INIT");
        this.videoEncodingService = videoEncodingService;
        this.videoDao = videoDao;
        this.s3StorageService = s3StorageService;
    }

    @Override
    public Void apply(S3Event s3Event) {
        log.info("---- HANDLING VIDEO UPLOAD EVENT");
        for (S3EventNotification.S3EventNotificationRecord record : s3Event.getRecords()) {
            String bucketName = record.getS3().getBucket().getName();
            String key = record.getS3().getObject().getKey();
            Optional<Video> videoOptional = videoDao.findByVideoS3Key(key);
            if (videoOptional.isEmpty()) {
                s3StorageService.deleteObject(key, bucketName);
                continue;
            }
            videoEncodingService.encodeVideo(videoOptional.get());
        }
        return null;
    }
}
