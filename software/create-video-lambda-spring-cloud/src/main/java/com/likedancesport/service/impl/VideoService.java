package com.likedancesport.service.impl;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.likedancesport.dao.IVideoDao;
import com.likedancesport.model.CreateVideoRequest;
import com.likedancesport.model.CreateVideoResult;
import com.likedancesport.model.impl.Video;
import com.likedancesport.parameter.annotation.InjectSsmParameter;
import com.likedancesport.service.IVideoService;
import com.likedancesport.service.storage.S3StorageService;
import com.likedancesport.utils.CreationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URL;

@Service
public class VideoService implements IVideoService {
    private final IVideoDao videoDao;
    private final S3StorageService s3StorageService;
    @InjectSsmParameter(parameterName = "s3-bucket-name")
    private String bucketName;

    @Autowired
    public VideoService(IVideoDao videoDao, S3StorageService s3StorageService, AWSSimpleSystemsManagement ssm) {
        this.videoDao = videoDao;
        this.s3StorageService = s3StorageService;
    }

    @Override
    public CreateVideoResult createVideo(CreateVideoRequest createVideoRequest) {
        Video video = CreationUtils.createVideoFromRequest(createVideoRequest);
        Video savedVideo = videoDao.save(video);
        URL presignedUrl = s3StorageService.generatePresignedUploadUrl(savedVideo, bucketName);
        return CreationUtils.createResultFromVideo(video, presignedUrl);
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println(bucketName);
    }
}
