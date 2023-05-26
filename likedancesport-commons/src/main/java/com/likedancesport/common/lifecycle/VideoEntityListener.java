package com.likedancesport.common.lifecycle;

import com.likedancesport.common.annotation.InjectSsmParameter;
import com.likedancesport.common.enums.VideoStatus;
import com.likedancesport.common.model.domain.S3Key;
import com.likedancesport.common.model.domain.learning.Video;
import com.likedancesport.common.service.S3StorageService;
import com.likedancesport.common.utils.ParameterNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import java.util.UUID;

@Component
public class VideoEntityListener  {
    @InjectSsmParameter(parameterName = ParameterNames.MP4_ASSETS_BUCKET_NAME, encrypted = true)
    private String mp4AssetsBucketName;

    private final S3StorageService s3StorageService;

    @Autowired
    public VideoEntityListener(S3StorageService s3StorageService) {
        this.s3StorageService = s3StorageService;
    }

    @PreRemove
    public void deleteAssets(Video video) {
        s3StorageService.deleteObject(video.getMp4AssetS3Key());
        video.getHlsGroup().getHlsVideos().forEach((s, s3Key) -> s3StorageService.deleteObject(s3Key));
    }

    @PrePersist
    public void generateAssetS3Key(Video video){
        //TODO: mp4 specification (maybe)
        S3Key assetS3Key = S3Key.of(mp4AssetsBucketName, "learning-assets/" + UUID.randomUUID());
        video.setMp4AssetS3Key(assetS3Key);
        video.setStatus(VideoStatus.UPLOADING);
    }
}
