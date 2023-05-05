package com.likedancesport.common.lifecycle;

import com.likedancesport.common.annotation.InjectSsmParameter;
import com.likedancesport.common.enums.VideoStatus;
import com.likedancesport.common.model.domain.S3Key;
import com.likedancesport.common.model.domain.learning.Video;
import com.likedancesport.common.service.S3StorageService;
import com.likedancesport.common.utils.constants.ParameterNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class VideoEntityListener extends BaseS3StorableEntityListener<Video> {
    @InjectSsmParameter(parameterName = ParameterNames.MP4_ASSETS_BUCKET_NAME, encrypted = true)
    private String mp4AssetsBucketName;
    @Autowired
    public VideoEntityListener(S3StorageService s3StorageService) {
        super(s3StorageService);
    }

    @Override
    public void deleteAssets(Video video) {
        super.deleteAssets(video);

    }

    @Override
    public void prePersist(Video video){
        super.prePersist(video);
        S3Key assetS3Key = S3Key.of(mp4AssetsBucketName, "learning-assets/" + UUID.randomUUID());
        video.setMp4AssetS3Key(assetS3Key);
        video.setStatus(VideoStatus.UPLOADING);
    }
}
