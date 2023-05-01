package com.likedancesport.service.impl;

import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.likedancesport.common.dao.ITranscodingJobDao;
import com.likedancesport.common.dao.IVideoDao;
import com.likedancesport.common.enums.VideoStatus;
import com.likedancesport.common.model.domain.learning.Video;
import com.likedancesport.common.model.internal.TranscodingJob;
import com.likedancesport.common.parameter.annotation.InjectSsmParameter;
import com.likedancesport.common.service.storage.S3StorageService;
import com.likedancesport.service.IVideoProcessingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.mediaconvert.MediaConvertClient;
import software.amazon.awssdk.services.mediaconvert.model.CreateJobRequest;
import software.amazon.awssdk.services.mediaconvert.model.CreateJobResponse;
import software.amazon.awssdk.services.mediaconvert.model.Input;
import software.amazon.awssdk.services.mediaconvert.model.JobSettings;

import java.util.Optional;

@Service
public class ElementalMediaConvertVideoProcessingService implements IVideoProcessingService {
    private final IVideoDao videoDao;

    private final MediaConvertClient mediaConvertClient;

    private final ITranscodingJobDao transcodingJobDao;

    private final S3StorageService s3StorageService;

    @InjectSsmParameter(parameterName = "media-convert-role-arn", encrypted = true)
    private String mediaConvertRoleArn;

    @InjectSsmParameter(parameterName = "emc-job-template-name")
    private String emcJobTemplateName;

    public ElementalMediaConvertVideoProcessingService(IVideoDao videoDao, MediaConvertClient mediaConvertClient, ITranscodingJobDao transcodingJobDao, S3StorageService s3StorageService) {
        this.videoDao = videoDao;
        this.mediaConvertClient = mediaConvertClient;
        this.transcodingJobDao = transcodingJobDao;
        this.s3StorageService = s3StorageService;
    }

    @Override
    @Transactional
    public void processVideo(S3EventNotification.S3EventNotificationRecord record) {
        String bucketName = record.getS3().getBucket().getName();
        String videoS3Key = record.getS3().getObject().getKey();
        Optional<Video> videoOptional = videoDao.findByVideoS3Key(videoS3Key);
        if (videoOptional.isEmpty()) {
            s3StorageService.deleteObject(videoS3Key, bucketName);
            return;
        }
        Video video = videoOptional.get();

        String s3Key = video.getVideoS3Key();

        JobSettings jobSettings = JobSettings.builder().inputs(Input.builder()
                        .fileInput(s3Key)
                        .build())
                .build();

        CreateJobRequest createJobRequest = CreateJobRequest.builder()
                .role(mediaConvertRoleArn)
                .jobTemplate(emcJobTemplateName)
                .settings(jobSettings)
                .build();

        CreateJobResponse createJobResponse = mediaConvertClient.createJob(createJobRequest);
        String jobId = createJobResponse.job().id();
        TranscodingJob transcodingJob = TranscodingJob.builder()
                .jobId(jobId)
                .videoId(video.getId())
                .build();

        transcodingJobDao.save(transcodingJob);
        video.setStatus(VideoStatus.PROCESSING);
        videoDao.save(video);
    }
}