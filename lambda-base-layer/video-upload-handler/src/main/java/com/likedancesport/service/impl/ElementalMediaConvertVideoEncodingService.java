package com.likedancesport.service.impl;

import com.likedancesport.common.enums.VideoStatus;
import com.likedancesport.common.model.impl.Video;
import com.likedancesport.common.parameter.annotation.InjectSsmParameter;
import com.likedancesport.dao.IVideoDao;
import com.likedancesport.service.IVideoEncodingService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.mediaconvert.MediaConvertClient;
import software.amazon.awssdk.services.mediaconvert.model.CreateJobRequest;
import software.amazon.awssdk.services.mediaconvert.model.CreateJobResponse;
import software.amazon.awssdk.services.mediaconvert.model.Input;
import software.amazon.awssdk.services.mediaconvert.model.JobSettings;

@Service
public class ElementalMediaConvertVideoEncodingService implements IVideoEncodingService {
    private final IVideoDao videoDao;

    private final MediaConvertClient mediaConvertClient;

    @InjectSsmParameter(parameterName = "media-convert-role-arn", encrypted = true)
    private String mediaConvertRoleArn;

    @InjectSsmParameter(parameterName = "emc-job-template-name")
    private String emcJobTemplateName;

    public ElementalMediaConvertVideoEncodingService(IVideoDao videoDao, MediaConvertClient mediaConvertClient) {
        this.videoDao = videoDao;
        this.mediaConvertClient = mediaConvertClient;
    }

    @Override
    public void encodeVideo(Video video) {
        if (!videoDao.existsById(video.getId())) {
            throw new RuntimeException("No such video");
        }

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

        video.setStatus(VideoStatus.PROCESSING);
        videoDao.save(video);
    }
}