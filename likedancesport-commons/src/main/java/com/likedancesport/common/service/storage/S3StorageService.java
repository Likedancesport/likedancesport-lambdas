package com.likedancesport.common.service.storage;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.likedancesport.common.utils.datetime.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class S3StorageService {
    protected final AmazonS3 s3;

    @Autowired
    public S3StorageService(AmazonS3 s3) {
        this.s3 = s3;
    }

    public URL generatePresingedUploadUrl(String s3Key, String bucketName) {
        return generatePresignedUrl(bucketName, s3Key,
                DateTimeUtils.generateExpirationDate(5, ChronoUnit.HOURS),
                HttpMethod.PUT);
    }

    public URL generatePresignedUrl(String bucketName, String key, Date expirationDate, HttpMethod httpMethod) {
        return s3.generatePresignedUrl(bucketName, key, expirationDate, httpMethod);
    }

    public void deleteObject(String s3Key, String bucketName) {
        s3.deleteObject(bucketName, s3Key);
    }
}
