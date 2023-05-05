package com.likedancesport.common.model.domain;

public interface IPreviewable {
    S3Key getPhotoS3Key();
    void setPhotoS3Key(S3Key s3Key);
}
