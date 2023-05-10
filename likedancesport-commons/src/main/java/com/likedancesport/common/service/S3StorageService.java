package com.likedancesport.common.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.likedancesport.common.model.domain.S3Key;
import com.likedancesport.common.utils.datetime.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3StorageService {
    protected final AmazonS3 s3;

    @Autowired
    public S3StorageService(AmazonS3 s3) {
        this.s3 = s3;
    }

    public URL generatePresignedUploadUrl(S3Key s3Key) {
        return generatePresignedUploadUrl(s3Key.getKey(), s3Key.getBucketName());
    }

    public URL generatePresignedUploadUrl(String key, String bucketName) {
        return generatePresignedUrl(bucketName, key,
                DateTimeUtils.generateExpirationDate(5, ChronoUnit.HOURS),
                HttpMethod.PUT);
    }

    public URL generatePresignedUrl(String bucketName, String key, Date expirationDate, HttpMethod httpMethod) {
        return s3.generatePresignedUrl(bucketName, key, expirationDate, httpMethod);
    }

    public void deleteObject(S3Key s3Key) {
        deleteObject(s3Key.getKey(), s3Key.getBucketName());
    }

    public void deleteObject(String key, String bucketName) {
        s3.deleteObject(bucketName, key);
    }

    public void deleteByPrefix(String bucketName, String prefix) {
        ObjectListing objectListing = s3.listObjects(bucketName, prefix);
        List<String> keys = objectListing.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName)
                .withKeys(keys.toArray(new String[0]));
        s3.deleteObjects(deleteObjectsRequest);
    }
}
