package com.likedancesport.common.service.storage;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.likedancesport.common.model.S3Storable;
import com.likedancesport.common.utils.datetime.DateTimeUtils;

import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class S3StorageService {
    protected final AmazonS3 s3;

    public S3StorageService(AmazonS3 s3) {
        this.s3 = s3;
    }

    public URL generatePresignedUploadUrl(S3Storable item, String bucketName) {
        return generatePresignedUrl(bucketName, item.getS3Key(),
                DateTimeUtils.generateExpirationDate(5, ChronoUnit.HOURS),
                HttpMethod.PUT);
    }

    public URL generatePresignedUrl(String bucketName, String key, Date expirationDate, HttpMethod httpMethod) {
        return s3.generatePresignedUrl(bucketName, key, expirationDate, httpMethod);
    }

    public void deleteObject(S3Storable item, String bucketName) {
        s3.deleteObject(bucketName, item.getS3Key());
    }
}
