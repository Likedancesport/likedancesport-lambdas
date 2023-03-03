package com.likedancesport.utils;

import com.likedancesport.model.CreateVideoRequest;
import com.likedancesport.model.CreateVideoResult;
import com.likedancesport.common.model.impl.Video;

import java.net.URL;

public class CreationUtils {
    public static Video createVideoFromRequest(CreateVideoRequest createVideoRequest) {
        return Video.builder()
                .title(createVideoRequest.getTitle())
                .s3Key(createVideoRequest.getTitle())
                .description(createVideoRequest.getDescription())
                .videoTags(createVideoRequest.getTags())
                .durationSeconds(createVideoRequest.getDurationSeconds())
                .build();
    }

    public static CreateVideoResult createResultFromVideo(Video video, URL presignedUrl) {
        return CreateVideoResult.builder()
                .id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .s3Key(video.getS3Key())
                .tags(video.getVideoTags())
                .presignedUrl(presignedUrl)
                .durationSeconds(video.getDurationSeconds())
                .build();
    }
}
