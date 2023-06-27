package com.likedancesport.service.impl;

import com.likedancesport.common.dao.ITranscodingJobDao;
import com.likedancesport.common.dao.IVideoDao;
import com.likedancesport.common.enums.VideoStatus;
import com.likedancesport.common.model.domain.HlsGroup;
import com.likedancesport.common.model.domain.S3Key;
import com.likedancesport.common.model.domain.learning.Video;
import com.likedancesport.common.model.internal.TranscodingJob;
import com.likedancesport.common.utils.MediaConvertUtils;
import com.likedancesport.common.utils.S3Utils;
import com.likedancesport.common.aws.MediaConvertJobStateChangeEvent;
import com.likedancesport.common.aws.OutputDetail;
import com.likedancesport.common.aws.OutputGroupDetail;
import com.likedancesport.service.ITranscodingJobCompleteHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.OptionalLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
public class TranscodingJobCompleteHandlerService implements ITranscodingJobCompleteHandlerService {
    private final IVideoDao videoDao;
    private final ITranscodingJobDao transcodingJobDao;

    @Autowired
    public TranscodingJobCompleteHandlerService(IVideoDao videoDao, ITranscodingJobDao transcodingJobDao) {
        this.videoDao = videoDao;
        this.transcodingJobDao = transcodingJobDao;
    }

    @Override
    @Transactional
    public void handleJobComplete(MediaConvertJobStateChangeEvent mediaConvertJobStateChangeEvent) {
        String jobId = mediaConvertJobStateChangeEvent.getDetail().getJobId();
        TranscodingJob transcodingJob = transcodingJobDao.findById(jobId);
        Video video = videoDao.findById(transcodingJob.getVideoId()).orElseThrow();

        Duration videoDuration = MediaConvertUtils.getDuration(mediaConvertJobStateChangeEvent);
        HlsGroup hlsGroup = MediaConvertUtils.getHlsGroup(mediaConvertJobStateChangeEvent);

        video.setDurationSeconds(videoDuration.getSeconds());
        video.setHlsGroup(hlsGroup);
        video.setStatus(VideoStatus.READY);

        videoDao.save(video);
    }
}
