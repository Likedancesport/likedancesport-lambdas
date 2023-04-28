package com.likedancesport.service;

import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;

public interface IVideoProcessingService {
    void processVideo(S3EventNotification.S3EventNotificationRecord record);
}
