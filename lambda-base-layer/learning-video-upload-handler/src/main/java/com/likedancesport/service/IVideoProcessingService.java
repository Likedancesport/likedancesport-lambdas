package com.likedancesport.service;

import com.likedancesport.common.model.domain.S3Key;

public interface IVideoProcessingService {
    void processVideo(S3Key s3Key);
}
