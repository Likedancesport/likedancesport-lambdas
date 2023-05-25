package com.likedancesport.service.impl;

import com.likedancesport.common.annotation.InjectSsmParameter;
import com.likedancesport.common.dao.ITranscodingJobDao;
import com.likedancesport.common.dao.IVideoDao;
import com.likedancesport.common.enums.VideoStatus;
import com.likedancesport.common.model.domain.S3Key;
import com.likedancesport.common.model.domain.learning.Video;
import com.likedancesport.common.model.internal.TranscodingJob;
import com.likedancesport.common.service.S3StorageService;
import com.likedancesport.common.utils.MediaConvertUtils;
import com.likedancesport.common.utils.ParameterNames;
import com.likedancesport.service.IVideoProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.mediaconvert.MediaConvertClient;
import software.amazon.awssdk.services.mediaconvert.model.AudioDefaultSelection;
import software.amazon.awssdk.services.mediaconvert.model.AudioSelector;
import software.amazon.awssdk.services.mediaconvert.model.ColorSpace;
import software.amazon.awssdk.services.mediaconvert.model.CreateJobRequest;
import software.amazon.awssdk.services.mediaconvert.model.CreateJobResponse;
import software.amazon.awssdk.services.mediaconvert.model.DescribeEndpointsRequest;
import software.amazon.awssdk.services.mediaconvert.model.DescribeEndpointsResponse;
import software.amazon.awssdk.services.mediaconvert.model.HlsCaptionLanguageSetting;
import software.amazon.awssdk.services.mediaconvert.model.HlsClientCache;
import software.amazon.awssdk.services.mediaconvert.model.HlsCodecSpecification;
import software.amazon.awssdk.services.mediaconvert.model.HlsDirectoryStructure;
import software.amazon.awssdk.services.mediaconvert.model.HlsGroupSettings;
import software.amazon.awssdk.services.mediaconvert.model.HlsManifestCompression;
import software.amazon.awssdk.services.mediaconvert.model.HlsManifestDurationFormat;
import software.amazon.awssdk.services.mediaconvert.model.HlsOutputSelection;
import software.amazon.awssdk.services.mediaconvert.model.HlsProgramDateTime;
import software.amazon.awssdk.services.mediaconvert.model.HlsSegmentControl;
import software.amazon.awssdk.services.mediaconvert.model.HlsStreamInfResolution;
import software.amazon.awssdk.services.mediaconvert.model.HlsTimedMetadataId3Frame;
import software.amazon.awssdk.services.mediaconvert.model.Input;
import software.amazon.awssdk.services.mediaconvert.model.InputDeblockFilter;
import software.amazon.awssdk.services.mediaconvert.model.InputDenoiseFilter;
import software.amazon.awssdk.services.mediaconvert.model.InputFilterEnable;
import software.amazon.awssdk.services.mediaconvert.model.InputPsiControl;
import software.amazon.awssdk.services.mediaconvert.model.InputRotate;
import software.amazon.awssdk.services.mediaconvert.model.InputTimecodeSource;
import software.amazon.awssdk.services.mediaconvert.model.JobSettings;
import software.amazon.awssdk.services.mediaconvert.model.Output;
import software.amazon.awssdk.services.mediaconvert.model.OutputGroup;
import software.amazon.awssdk.services.mediaconvert.model.OutputGroupSettings;
import software.amazon.awssdk.services.mediaconvert.model.OutputGroupType;
import software.amazon.awssdk.services.mediaconvert.model.VideoSelector;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ElementalMediaConvertVideoProcessingService implements IVideoProcessingService {
    private final IVideoDao videoDao;

    private final ITranscodingJobDao transcodingJobDao;

    private final S3StorageService s3StorageService;

    @InjectSsmParameter(parameterName = "media-convert-role-arn", encrypted = true)
    private String mediaConvertRoleArn;

    @InjectSsmParameter(parameterName = "emc-job-template-name")
    private String emcJobTemplateName;

    @InjectSsmParameter(parameterName = ParameterNames.HLS_LEARNING_VIDEOS_BUCKET_NAME)
    private String hlsBucketName;

    public ElementalMediaConvertVideoProcessingService(IVideoDao videoDao, ITranscodingJobDao transcodingJobDao, S3StorageService s3StorageService) {
        this.videoDao = videoDao;
        this.transcodingJobDao = transcodingJobDao;
        this.s3StorageService = s3StorageService;
    }

    @Override
    @Transactional
    public void processVideo(S3Key s3Key) {
        log.info("---- PROCESSING VIDEO");
        try (MediaConvertClient mc = MediaConvertClient.create()) {

            DescribeEndpointsResponse res = mc
                    .describeEndpoints(DescribeEndpointsRequest.builder().maxResults(20).build());
            String endpointURL = res.endpoints().get(0).url();

            MediaConvertClient mediaConvertClient = MediaConvertClient.builder()
                    .region(mc.serviceClientConfiguration().region())
                    .endpointOverride(URI.create(endpointURL))
                    .build();

            Video video = getVideo(s3Key);

            if (video == null) {
                return;
            }

            S3Key key = video.getMp4AssetS3Key();

            CreateJobRequest createJobRequest = getCreateJobRequest(key);

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

    private CreateJobRequest getCreateJobRequest(S3Key key) {
        OutputGroup appleHLS = getOutputGroup();

        Map<String, AudioSelector> audioSelectors = new HashMap<>();
        audioSelectors.put("Audio Selector 1",
                AudioSelector.builder().defaultSelection(AudioDefaultSelection.DEFAULT).offset(0).build());

        JobSettings jobSettings = JobSettings.builder().inputs(Input.builder().audioSelectors(audioSelectors)
                        .videoSelector(
                                VideoSelector.builder().colorSpace(ColorSpace.FOLLOW).rotate(InputRotate.DEGREE_0).build())
                        .filterEnable(InputFilterEnable.AUTO).filterStrength(0).deblockFilter(InputDeblockFilter.DISABLED)
                        .denoiseFilter(InputDenoiseFilter.DISABLED).psiControl(InputPsiControl.USE_PSI)
                        .timecodeSource(InputTimecodeSource.EMBEDDED)
                        .fileInput(key.getUri()).build())
                .outputGroups(appleHLS).build();

        return CreateJobRequest.builder()
                .role(mediaConvertRoleArn)
                .settings(jobSettings)
                .build();
    }

    private Video getVideo(S3Key s3Key) {
        Optional<Video> videoOptional = videoDao.findByMp4AssetS3Key(s3Key);

        if (videoOptional.isEmpty()) {
            s3StorageService.deleteObject(s3Key);
            return null;
        }

        return videoOptional.get();
    }

    private OutputGroup getOutputGroup() {
        Output output720p = MediaConvertUtils.createOutput("720p", "720p",
                1200000, 7, 1280, 720);

        Output output1080p = MediaConvertUtils.createOutput("1080p", "_1080p",
                3500000, 8, 1920, 1080);

        String destination = "s3://" + hlsBucketName + "/" + UUID.randomUUID() + "/";

        return OutputGroup.builder().name("Apple HLS").customName("Example")
                .outputGroupSettings(OutputGroupSettings.builder().type(OutputGroupType.HLS_GROUP_SETTINGS)
                        .hlsGroupSettings(HlsGroupSettings.builder()
                                .destination(destination)
                                .directoryStructure(HlsDirectoryStructure.SINGLE_DIRECTORY)
                                .manifestDurationFormat(HlsManifestDurationFormat.INTEGER)
                                .streamInfResolution(HlsStreamInfResolution.INCLUDE)
                                .clientCache(HlsClientCache.ENABLED)
                                .captionLanguageSetting(HlsCaptionLanguageSetting.OMIT)
                                .manifestCompression(HlsManifestCompression.NONE)
                                .codecSpecification(HlsCodecSpecification.RFC_4281)
                                .outputSelection(HlsOutputSelection.MANIFESTS_AND_SEGMENTS)
                                .programDateTime(HlsProgramDateTime.EXCLUDE)
                                .programDateTimePeriod(600)
                                .timedMetadataId3Frame(HlsTimedMetadataId3Frame.PRIV)
                                .timedMetadataId3Period(10)
                                .segmentControl(HlsSegmentControl.SEGMENTED_FILES)
                                .minFinalSegmentLength((double) 0)
                                .segmentLength(4)
                                .minSegmentLength(0)
                                .build())
                        .build())
                .outputs(output720p, output1080p)
                .build();
    }

}