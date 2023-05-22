package com.likedancesport.service;

import com.likedancesport.common.aws.MediaConvertJobStateChangeEvent;

public interface ITranscodingJobCompleteHandlerService {
    void handleJobComplete(MediaConvertJobStateChangeEvent mediaConvertJobStateChangeEvent);
}
