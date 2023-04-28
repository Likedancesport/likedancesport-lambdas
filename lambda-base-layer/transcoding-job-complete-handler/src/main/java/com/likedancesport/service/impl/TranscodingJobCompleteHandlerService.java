package com.likedancesport.service.impl;

import com.likedancesport.common.dao.ITranscodingJobDao;
import com.likedancesport.common.dao.IVideoDao;
import com.likedancesport.common.enums.VideoStatus;
import com.likedancesport.common.model.domain.impl.Video;
import com.likedancesport.common.model.internal.TranscodingJob;
import com.likedancesport.model.aws.MediaConvertJobStateChangeEvent;
import com.likedancesport.model.aws.OutputDetail;
import com.likedancesport.model.aws.OutputGroupDetail;
import com.likedancesport.service.ITranscodingJobCompleteHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.OptionalLong;
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
        Duration videoDuration = getDuration(mediaConvertJobStateChangeEvent);
        video.setDurationSeconds(videoDuration.getSeconds());
        video.setStatus(VideoStatus.READY);
        videoDao.save(video);
    }

    private Duration getDuration(MediaConvertJobStateChangeEvent event) {
        Long durationInMillis = event.getDetail().getOutputGroupDetails().stream()
                .map(OutputGroupDetail::getOutputDetails)
                .map(outputDetails -> outputDetails.stream().mapToLong(OutputDetail::getDurationInMs))
                .map(LongStream::max)
                .map(OptionalLong::orElseThrow)
                .max(Long::compareTo)
                .orElseThrow();
        return Duration.ofMillis(durationInMillis);
    }
}
