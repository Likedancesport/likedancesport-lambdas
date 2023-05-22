package com.likedancesport.common.lifecycle;

import com.likedancesport.common.annotation.InjectSsmParameter;
import com.likedancesport.common.model.domain.IPreviewable;
import com.likedancesport.common.model.domain.S3Key;
import com.likedancesport.common.service.S3StorageService;
import com.likedancesport.common.utils.ParameterNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import java.util.UUID;

@Component
public class BaseS3StorableEntityListener{
    protected final S3StorageService s3StorageService;
    @InjectSsmParameter(parameterName = ParameterNames.THUMBNAILS_BUCKET_NAME, encrypted = true)
    protected String thumbnailsBucketName;

    @Autowired
    public BaseS3StorableEntityListener(S3StorageService s3StorageService) {
        this.s3StorageService = s3StorageService;
    }

    @PreRemove
    public void deleteAssets(IPreviewable entity) {
        s3StorageService.deleteObject(entity.getPhotoS3Key());
    }

    @PrePersist
    public void prePersist(IPreviewable entity) {
        S3Key photoS3Key = S3Key.of(thumbnailsBucketName, "photos/" + UUID.randomUUID());
        entity.setPhotoS3Key(photoS3Key);
    }

}
